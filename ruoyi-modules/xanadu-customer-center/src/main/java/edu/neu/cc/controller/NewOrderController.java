package edu.neu.cc.controller;


import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.auth.AuthLogic;
import com.ruoyi.common.security.auth.AuthUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.cc.entity.*;
import edu.neu.cc.feign.WareCenterStorageRecordClient;
import edu.neu.cc.service.*;
import edu.neu.cc.vo.NewOrderVo;
import edu.neu.cc.vo.ProductRecordsVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/cc/newOrder")
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

    @PostMapping("/create")
    @ApiOperation("创建新订单")
    @ApiParam(name = "newOrder", value = "新订单信息")
    @Transactional
    public AjaxResult createNewOrder(@RequestBody NewOrderVo newOrderVo) {
        //客户希望创建订单，检查对应的商品是否缺货，如果缺货则生成缺货记录，否则订单状态为可分配
        if (newOrderVo == null) return AjaxResult.error("订单信息不能为空");
        //检查商品是否缺货,并根据其状态设置订单状态
        NewOrder newOrder = new NewOrder();
        BeanUtils.copyProperties(newOrderVo, newOrder);
        Order order = new Order();
        BeanUtils.copyProperties(newOrderVo, order);

        List<Product> products = newOrder.getProducts();
        if (products == null || products.size() == 0) return AjaxResult.error("订单中商品不能为空");
        //将商品ID和数量封装成键值对
        Map<Long, Integer> productIdNumberMap = products.stream().collect(Collectors.toMap(Product::getProductId, Product::getNumber));
        //调用远程接口，检查商品是否缺货
        ProductRecordsVo productRecordsVo = wareCenterStorageRecordClient.check(productIdNumberMap);
        if (productRecordsVo == null) return AjaxResult.error("商品信息不能为空");
        if (productRecordsVo.getIsLack()) newOrder.setStatus(OrderStatusConstant.OUT_OF_STOCK);
        else newOrder.setStatus(OrderStatusConstant.CAN_BE_ALLOCATED);
        //插入订单数据库
        if (!orderService.save(order)) throw new ServiceException("记录订单异常");
        newOrder.setId(order.getId());
        newOrderService.save(newOrder);
        Long userId = SecurityUtils.getUserId();
        //一开始是空，可以设置默认值
        //TODO: 由gateway设置用户ID，若未登陆则提示错误
        if (userId == null) userId = 1L;

        //插入对应的数据表，保存相关记录
        if (productRecordsVo.getIsLack()) {
            //生成缺货记录
            Long finalUserId = userId;
            products.forEach(product -> {
                product.setOrderId(order.getId());
                if (!productService.save(product)) throw new ServiceException("插入商品记录异常");
                if (productRecordsVo.getProductIdNumberMap().containsKey(product.getProductId())) {
                    //生成缺货记录
                    Stockout stockout = new Stockout();
                    stockout.setProductId(product.getProductId());
                    stockout.setOrderId(order.getId());
                    stockout.setNeedNumbers(productRecordsVo.getProductIdNumberMap().get(product.getProductId()));
                    stockout.setCreateBy(finalUserId);
                    stockout.setStatus(StockoutConstant.STOCKOUT);
                    //插入缺货记录
                    if (!stockoutService.save(stockout)) throw new ServiceException("生成缺货记录异常");

                }
            });
        }
        //生成操作记录,记录订单创建操作
        Operation operation = new Operation();
        operation.setOrderId(newOrder.getId());
        operation.setUserId(userId);
        operation.setOperatorType(OperationTypeConstant.ORDER);
        operation.setCustomerId(order.getCustomerId());
        operation.setTotalAmount(newOrder.getTotalAmount());
        if (!operationService.save(operation)) throw new ServiceException("生成操作记录异常");

        //填充newOrderID等字段,返回给前端
        newOrderVo.setId(newOrder.getId());
        newOrderVo.setCreateTime(newOrder.getCreateTime());
        newOrderVo.setStatus(newOrder.getStatus());

        //完成操作后返回成功结果
        return AjaxResult.success("创建订单成功", newOrderVo);
    }


}

