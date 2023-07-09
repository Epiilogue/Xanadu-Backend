package edu.neu.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import edu.neu.base.constant.cc.MQTopic;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.cc.entity.*;
import edu.neu.cc.feign.WareCenterStorageRecordClient;
import edu.neu.cc.service.*;
import edu.neu.cc.vo.DispatchMessage;
import edu.neu.cc.vo.NewOrderVo;
import edu.neu.cc.vo.ProductRecordsVo;
import edu.neu.cc.vo.UnSubscribeVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/newOrder")
@Transactional
public class NewOrderController {


    /**
     * 远程调用接口，能够检查库存数量
     */
    @Autowired
    WareCenterStorageRecordClient wareCenterStorageRecordClient;

    @Autowired
    OrderService orderService;

    @Autowired
    NewOrderService newOrderService;

    @Autowired
    ProductService productService;

    @Autowired
    StockoutService stockoutService;

    @Autowired
    OperationService operationService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PostMapping("/create")
    @ApiOperation("创建新订单")
    @ApiParam(name = "newOrder", value = "新订单信息")
    public AjaxResult createNewOrder(@RequestBody NewOrderVo newOrderVo) {
        //客户希望创建订单，检查对应的商品是否缺货，如果缺货则生成缺货记录，否则订单状态为可分配
        if (newOrderVo == null) return AjaxResult.error("订单信息不能为空");
        //检查商品是否缺货,并根据其状态设置订单状态
        NewOrder newOrder = new NewOrder();
        BeanUtils.copyProperties(newOrderVo, newOrder);

        Order order = new Order();
        BeanUtils.copyProperties(newOrderVo, order);
        List<Product> products = newOrder.getProducts();
        //过滤掉数量为0的商品
        products = products.stream().filter(p -> p.getNumber() > 0).collect(Collectors.toList());
        if (products.size() == 0) return AjaxResult.error("订单中商品数量不合法");

        //将商品ID和数量封装成键值对
        Map<Long, Integer> productIdNumberMap = products.stream().collect(Collectors.toMap(Product::getProductId, Product::getNumber));
        //调用远程接口，检查商品是否缺货
        ProductRecordsVo productRecordsVo = wareCenterStorageRecordClient.check(productIdNumberMap);
        if (productRecordsVo == null) return AjaxResult.error("商品信息不能为空");
        if (productRecordsVo.getIsLack()) order.setStatus(OrderStatusConstant.OUT_OF_STOCK);
        else {
            order.setStatus(OrderStatusConstant.CAN_BE_ALLOCATED);
            //需要为订单锁定库存
            productIdNumberMap.forEach((k, v) -> {
                Boolean success = wareCenterStorageRecordClient.lock(k, v);
                if (!success) throw new ServiceException("锁定库存失败");
            });


        }
        Long userId = SecurityUtils.getUserId();
        order.setUserId(userId);
        order.setOrderType(OperationTypeConstant.ORDER);
        //插入订单数据库
        orderService.save(order);
        newOrder.setId(order.getId());
        newOrderService.save(newOrder);


        //插入对应的数据表，保存相关记录
        if (productRecordsVo.getIsLack()) {
            //生成缺货记录
            Long finalUserId = userId;
            Map<Long, Integer> lackMap = productRecordsVo.getProductIdNumberMap();
            products.forEach(product -> {
                if (lackMap.containsKey(product.getProductId())) product.setIslack(true);
                product.setOrderId(order.getId());
                if (!productService.save(product)) throw new ServiceException("插入商品记录异常");
                if (lackMap.containsKey(product.getProductId())) {
                    //生成缺货记录
                    Stockout stockout = new Stockout();
                    stockout.setProductId(product.getProductId());
                    stockout.setOrderId(order.getId());
                    stockout.setNeedNumbers(lackMap.get(product.getProductId()));
                    stockout.setCreateBy(finalUserId);
                    stockout.setStatus(StockoutConstant.UNCOMMITTED);
                    //插入缺货记录
                    stockoutService.save(stockout);
                }
            });
        } else {
            products.forEach(product -> {
                product.setOrderId(order.getId());
                if (!productService.save(product)) throw new ServiceException("插入商品记录异常");
            });
        }
        //生成操作记录,记录订单创建操作
        Operation operation = new Operation();
        operation.setOrderId(order.getId());
        operation.setUserId(userId);
        operation.setOperatorType(OperationTypeConstant.ORDER);
        operation.setCustomerId(order.getCustomerId());
        operation.setTotalAmount(order.getTotalAmount());
        operation.setNumbers(order.getNumbers());//number是订单中商品的总数

        //发送到消息队列
        rocketMQTemplate.syncSend(MQTopic.OPERATION_TOPIC, operation);

        //填充newOrderID等字段,返回给前端
        newOrderVo.setId(newOrder.getId());
        newOrderVo.setCreateTime(order.getCreateTime());
        newOrderVo.setStatus(order.getStatus());

        //检查订单状态是不是可分配，并且是否已经指定了分站，将订单封装后发送到消息队列中，需要的信息为订单ID和分站ID
        if (order.getStatus().equals(OrderStatusConstant.CAN_BE_ALLOCATED) && newOrder.getSubstationId() != null) {
            try {
                DispatchMessage dispatchVo = new DispatchMessage(order.getId(), newOrder.getSubstationId());
                MessageBuilder<DispatchMessage> builder = MessageBuilder.withPayload(dispatchVo);
                rocketMQTemplate.syncSend(MQTopic.ORDER_TOPIC, builder.build(), 3000, 7);//发送延迟消息到消息队列，延迟两分钟
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //完成操作后返回成功结果
        return AjaxResult.success("创建订单成功", newOrderVo);
    }


    @ApiOperation("撤销订单")
    @GetMapping("/cancel/{orderId}")
    public AjaxResult cancelOrder(@PathVariable("orderId") Long orderId) {
        //检查orderId是否存在
        if (orderId == null) return AjaxResult.error("订单ID不能为空");
        //检查是否是正确的订单号
        Order order = orderService.getById(orderId);
        if (order == null) return AjaxResult.error("订单不存在");
        //检查订单状态是否是可撤销状态，只有缺货、可分配的订单才能撤销
        if (!Objects.equals(order.getStatus(), OrderStatusConstant.OUT_OF_STOCK) && !Objects.equals(order.getStatus(), OrderStatusConstant.CAN_BE_ALLOCATED))
            return AjaxResult.error("订单状态不可撤销");
        Long userId = SecurityUtils.getUserId();
        //撤销订单,删除订单以及相关的缺货记录、商品记录
        //删除订单
        orderService.removeById(orderId);

/*
        newOrderService.removeById(orderId);
        productService.remove(new QueryWrapper<Product>().eq("order_id", orderId));
*/

        //删除缺货记录，只有状态为未提交才允许删除
        stockoutService.remove(new QueryWrapper<Stockout>().eq("order_id", orderId).eq("status", StockoutConstant.UNCOMMITTED));
        //如果订单为可分配状态，还需要撤销锁定的库存
        if (order.getStatus().equals(OrderStatusConstant.CAN_BE_ALLOCATED)) {
            //获取订单中的商品ID和数量
            productService.list(new QueryWrapper<Product>().eq("order_id", orderId)).forEach(
                    product -> {
                        Boolean unlock = wareCenterStorageRecordClient.unlock(product.getProductId(), product.getNumber());
                        if (!unlock) throw new ServiceException("撤销失败，库存解锁失败");
                    }
            );
        }
        //生成操作记录,记录订单撤销操作
        Operation operation = new Operation();
        operation.setOrderId(orderId);
        operation.setOperatorType(OperationTypeConstant.CANCEL);
        operation.setUserId(userId);
        operation.setCustomerId(order.getCustomerId());
        operation.setTotalAmount(order.getTotalAmount());
        operation.setNumbers(order.getNumbers());//number是订单中商品的总数

        //发送到消息队列
        rocketMQTemplate.syncSend(MQTopic.OPERATION_TOPIC, operation);

        return AjaxResult.success("撤销订单成功");
    }


    @ApiOperation("退订订单")
    @PostMapping("/unsubscribe")
    public AjaxResult unsubscribeOrder(@RequestBody UnSubscribeVo unSubscribeVo) {
        if (unSubscribeVo == null) return AjaxResult.error("退订信息不能为空");
        Long orderId = unSubscribeVo.getOrderId();

        //检查orderId是否存在
        if (orderId == null) return AjaxResult.error("订单ID不能为空");
        //检查是否是正确的订单号
        Order order = orderService.getById(orderId);
        if (order == null) return AjaxResult.error("订单不存在");
        //检查订单状态是否是可撤销状态，只有缺货、可分配的订单才能撤销
        if (!Objects.equals(order.getStatus(), OrderStatusConstant.OUT_OF_STOCK) && !Objects.equals(order.getStatus(), OrderStatusConstant.CAN_BE_ALLOCATED))
            return AjaxResult.error("当前订单状态不可退订");

        //获取订单对应的商品列表
        List<Product> products = productService.list(new QueryWrapper<Product>().eq("order_id", orderId));
        //将商品ID和数量封装成键值对
        Map<Long, Integer> productIdNumberMap = products.stream().collect(Collectors.toMap(Product::getProductId, Product::getNumber));
        //将商品ID和价格封装成键值对
        Map<Long, Double> productIdPriceMap = products.stream().collect(Collectors.toMap(Product::getProductId, Product::getPrice));

        //检查退订数量是否超过订购数量
        for (Map.Entry<Long, Integer> entry : unSubscribeVo.getProductsMap().entrySet()) {
            if (entry.getValue() > productIdNumberMap.get(entry.getKey()))
                return AjaxResult.error(String.format("商品ID-%d 的退订数量超过订购数量", entry.getKey()));
        }
        //退订订单，插入退订记录，更新原订单状态
        Long userId = SecurityUtils.getUserId();

        //生成一条新的order记录，
        Order unsubscribeOrder = new Order();
        unsubscribeOrder.setCustomerId(order.getCustomerId());
        unsubscribeOrder.setTotalAmount(unSubscribeVo.getTotalAmount(productIdPriceMap));

        unsubscribeOrder.setUserId(userId);
        unsubscribeOrder.setStatus(OrderStatusConstant.INVALID);//无效订单
        unsubscribeOrder.setOrderType(OperationTypeConstant.UNSUBSCRIBE);//退订订单
        orderService.save(unsubscribeOrder);
        //生成一条refund记录
        Refund refund = new Refund();
        BeanUtils.copyProperties(unSubscribeVo, refund);
        refund.setId(unsubscribeOrder.getId());
        refund.setOrderId(order.getId());
        refundService.save(refund);//插入退订记录

        //插入商品
        unSubscribeVo.getProductsMap().forEach((productId, number) -> {
            Product product = new Product();
            product.setOrderId(unsubscribeOrder.getId());
            product.setProductId(productId);
            product.setNumber(number);
            product.setPrice(productIdPriceMap.get(productId));
            productService.save(product);
            //如果原订单状态为可分配订单，还需要解锁库存
            if (order.getStatus().equals(OrderStatusConstant.CAN_BE_ALLOCATED)) {
                Boolean unlock = wareCenterStorageRecordClient.unlock(productId, number);
                if (!unlock) throw new ServiceException("退订失败，库存解锁失败");
            }
        });
        //更新原订单状态，例如商品数量、总价等，更新商品数量，更新缺货记录，添加操作记录
        order.setNumbers(order.getNumbers() - unsubscribeOrder.getNumbers());
        order.setTotalAmount(order.getTotalAmount() - unsubscribeOrder.getTotalAmount());

        //更新map,productIdNumberMap存的是新的order中商品的数量
        unSubscribeVo.getProductsMap().forEach((productId, number) -> productIdNumberMap.put(productId, productIdNumberMap.get(productId) - number));
        //发起远程调用，获取新的状态
        ProductRecordsVo result = wareCenterStorageRecordClient.check(productIdNumberMap);

        //如果退货后不缺货了，需要删除掉之前所有的未提交的缺货记录，订单状态改为已分配，并重新锁定库存
        if (!result.getIsLack() && order.getStatus().equals(OrderStatusConstant.OUT_OF_STOCK)) {
            //删除所有原来的未提交的缺货记录
            stockoutService.remove(new QueryWrapper<Stockout>().eq("order_id", orderId).eq("status", StockoutConstant.UNCOMMITTED));
            order.setStatus(OrderStatusConstant.CAN_BE_ALLOCATED);
            //锁定库存
            productIdNumberMap.forEach((productId, number) -> {
                Boolean lock = wareCenterStorageRecordClient.lock(productId, number);
                if (!lock) throw new ServiceException("退订失败，库存锁定失败");
            });
        }
        //如果退货后仍然缺货，说明之前也肯定缺货，但是会有商品不缺货了，我们需要删除所有的stockout记录，然后重新插入缺货记录
        if (result.getIsLack() && order.getStatus().equals(OrderStatusConstant.OUT_OF_STOCK)) {
            //删除所有原来的未提交的缺货记录
            stockoutService.remove(new QueryWrapper<Stockout>().eq("order_id", orderId).eq("status", StockoutConstant.UNCOMMITTED));
            //重新插入缺货记录
            productIdNumberMap.forEach((productId, number) -> {
                Stockout stockout = new Stockout();
                stockout.setOrderId(orderId);
                stockout.setProductId(productId);
                stockout.setNeedNumbers(result.getProductIdNumberMap().get(productId));
                stockout.setStatus(StockoutConstant.UNCOMMITTED);
                stockoutService.save(stockout);
            });
        }

        orderService.updateById(order);//更新原订单
        //更新原订单的商品记录
        Map<Long, Integer> idNumberMap = result.getProductIdNumberMap();
        productIdNumberMap.forEach((productId, number) -> {
            Product product = productService.getOne(new QueryWrapper<Product>().eq("order_id", orderId).eq("product_id", productId));
            product.setNumber(number);
            if (idNumberMap.containsKey(productId)) product.setIslack(true);
            if (product.getNumber() == 0) {
                productService.removeById(product.getId());
            } else {
                productService.updateById(product);
            }
        });

        //生成操作记录,记录订单退订操作
        Operation operation = new Operation();
        operation.setOrderId(unsubscribeOrder.getId());
        operation.setOperatorType(OperationTypeConstant.UNSUBSCRIBE);
        operation.setUserId(userId);
        operation.setCustomerId(unsubscribeOrder.getCustomerId());
        operation.setTotalAmount(unsubscribeOrder.getTotalAmount());
        operation.setNumbers(unsubscribeOrder.getNumbers());//number是订单中商品的总数

        //发送到消息队列
        rocketMQTemplate.syncSend(MQTopic.OPERATION_TOPIC, operation);

        //退订成功
        return AjaxResult.success("退订成功");
    }


}

