package edu.neu.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import edu.neu.base.constant.cc.NewOrderType;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.cc.entity.*;
import edu.neu.cc.feign.WareCenterStorageRecordClient;
import edu.neu.cc.service.*;
import edu.neu.cc.vo.ProductRecordsVo;
import edu.neu.cc.vo.RefundVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/cc/refund")
@Api(tags = "RefundController", description = "处理退货换货")
@Transactional
public class RefundController {

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


    //换货申请业务逻辑，生成新订单，绑定原订单，需要检查原来的订单状态，检查商品是否可以换货，检查商品是否缺货
    //若商品缺货，则需要提交缺货申请，换货最后还需要把原来的商品要回来，做一个后续的退货处理
    @PostMapping("/exchange")
    @ApiOperation("换货申请")
    public AjaxResult exchange(@RequestBody RefundVo refundVo) {
        //1.前端已经拿到了原来的订单信息，并且完成了校验不允许传入非换货商品，并且完成了订单状态的检查
        // 只有完成状态的订单才允许换货
        //客户希望创建订单，检查对应的商品是否缺货，如果缺货则生成缺货记录，否则订单状态为可分配
        if (refundVo == null) return AjaxResult.error("换货信息不能为空");

        Order preOrder = orderService.getById(refundVo.getOrderId());
        if (preOrder == null) return AjaxResult.error("原订单不存在");
        //检查订单状态
        String status = preOrder.getStatus();
        if (!(status.equals(OrderStatusConstant.COMPLETED)))
            return AjaxResult.error("原订单状态未完成，不允许换货");
        if (!preOrder.getOrderType().equals(OperationTypeConstant.ORDER))
            return AjaxResult.error("原订单不是新订类型，不允许换货");

        NewOrder newOrder = newOrderService.getById(preOrder.getId());
        if (newOrder == null) return AjaxResult.error("原订单不存在");
        Long substationId = newOrder.getSubstationId();
        Order order = new Order();
        Refund refund = new Refund();
        BeanUtils.copyProperties(refundVo, order);
        BeanUtils.copyProperties(refundVo, refund);
        List<Product> products = refundVo.getProducts();
        products = products.stream().filter(p -> p.getNumber() > 0).collect(Collectors.toList());
        if (products.size() == 0) return AjaxResult.error("换货商品数量为0");

        products.forEach(p -> {
            if (!p.getChangeAble()) throw new ServiceException("存在商品不允许换货");
        });
        //拿到原来的订单商品
        List<Product> productList = productService.list(new QueryWrapper<Product>().eq("order_id", refundVo.getOrderId()));
        //检查对应商品数量是不是小于换货数量，超过了不允许换
        for (Product product : products) {
            for (Product p : productList) {
                if (product.getProductId().equals(p.getProductId())) {
                    if (product.getNumber() > p.getNumber()) return AjaxResult.error("换货数量超过原订单数量");
                }
            }
        }

        //将商品ID和数量封装成键值对
        Map<Long, Integer> productIdNumberMap = products.stream().collect(Collectors.toMap(Product::getProductId, Product::getNumber));
        //调用远程接口，检查商品是否缺货
        ProductRecordsVo productRecordsVo = wareCenterStorageRecordClient.check(productIdNumberMap);
        if (productRecordsVo == null) return AjaxResult.error("商品信息不能为空");
        if (productRecordsVo.getIsLack()) order.setStatus(OrderStatusConstant.OUT_OF_STOCK);
        else order.setStatus(OrderStatusConstant.CAN_BE_ALLOCATED);
        order.setOrderType(OperationTypeConstant.EXCHANGE);
        //插入换货数据库
        orderService.save(order);
        refund.setOrderId(order.getId());
        refund.setOperationType(OperationTypeConstant.RETURN);
        refund.setSubstationId(substationId);

        refundService.save(refund);
        Long userId = SecurityUtils.getUserId();
        //一开始是空，可以设置默认值
        //TODO: 由gateway设置用户ID，若未登陆则提示错误
        if (userId == null) userId = 1L;
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
        }
        //生成操作记录,记录订单创建操作
        Operation operation = new Operation(order.getId(), order.getCustomerId(), order.getId(),
                OperationTypeConstant.EXCHANGE, order.getNumbers(), order.getTotalAmount());
        operationService.save(operation);
        //填充newOrderID等字段,返回给前端
        refundVo.setId(order.getId());
        refundVo.setCreateTime(order.getCreateTime());
        refundVo.setStatus(order.getStatus());

        //完成操作后返回成功结果
        return AjaxResult.success("创建换货订单成功", refundVo);
    }

    //退货申请，创建退货订单，插入商品，直接可分配状态
    @PostMapping("/refund")
    @ApiOperation("退货申请")
    public AjaxResult refund(@RequestBody RefundVo refundVo) {
        //1.前端已经拿到了原来的订单信息，并且完成了校验不允许传入非换货商品，并且完成了订单状态的检查
        // 只有完成状态的订单才允许换货
        //客户希望创建订单，检查对应的商品是否缺货，如果缺货则生成缺货记录，否则订单状态为可分配
        if (refundVo == null) return AjaxResult.error("退货信息不能为空");


        Order preOrder = orderService.getById(refundVo.getOrderId());
        if (preOrder == null) return AjaxResult.error("原订单不存在");
        //检查订单状态
        String status = preOrder.getStatus();
        if (!(status.equals(OrderStatusConstant.COMPLETED)))
            return AjaxResult.error("原订单状态未完成，不允许换货");
        if (!preOrder.getOrderType().equals(OperationTypeConstant.ORDER))
            return AjaxResult.error("原订单不是新订类型，不允许换货");

        NewOrder newOrder = newOrderService.getById(preOrder.getId());
        if (newOrder == null) return AjaxResult.error("原订单不存在");
        Long substationId = newOrder.getSubstationId();
        Order order = new Order();
        Refund refund = new Refund();
        BeanUtils.copyProperties(refundVo, order);
        BeanUtils.copyProperties(refundVo, refund);
        List<Product> products = refundVo.getProducts();
        products = products.stream().filter(p -> p.getNumber() > 0).collect(Collectors.toList());
        if (products.size() == 0) return AjaxResult.error("换货商品数量为0");
        products.forEach(p -> {
            if (!p.getRefundAble()) throw new ServiceException("存在商品不允许退货");
        });
        //拿到原来的订单商品
        List<Product> productList = productService.list(new QueryWrapper<Product>().eq("order_id", refundVo.getOrderId()));
        //检查对应商品数量是不是小于退货数量，超过了不允许换
        for (Product product : products) {
            for (Product p : productList) {
                if (product.getProductId().equals(p.getProductId())) {
                    if (product.getNumber() > p.getNumber()) return AjaxResult.error("换货数量超过原订单数量");
                }
            }
        }
        products.forEach(p -> {
            //插入商品记录
            p.setOrderId(order.getId());
            if (!productService.save(p)) throw new ServiceException("插入商品记录异常");
        });

        order.setStatus(OrderStatusConstant.CAN_BE_ALLOCATED);
        order.setOrderType(OperationTypeConstant.RETURN);
        //插入订单数据库
        orderService.save(order);
        refund.setOrderId(order.getId());
        refund.setOperationType(OperationTypeConstant.RETURN);
        refund.setSubstationId(substationId);
        refundService.save(refund);

        refundVo.setId(order.getId());
        refundVo.setCreateTime(order.getCreateTime());
        refundVo.setStatus(order.getStatus());
        return AjaxResult.success("创建退货订单成功", refundVo);
    }


}

