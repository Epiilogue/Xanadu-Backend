package edu.neu.cc.controller;


import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.cc.entity.NewOrder;
import edu.neu.cc.entity.Order;
import edu.neu.cc.entity.Product;
import edu.neu.cc.entity.Refund;
import edu.neu.cc.service.NewOrderService;
import edu.neu.cc.service.OrderService;
import edu.neu.cc.service.ProductService;
import edu.neu.cc.service.RefundService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/order")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private NewOrderService newOrderService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private ProductService productService;

    @ApiOperation("根据客户ID，获取订单列表，如果客户ID为空，则获取所有订单列表")
    @GetMapping("/list/{customerId}")
    public AjaxResult getOrderListByCustomerId(@PathVariable(required = false) Long customerId) {
        if (customerId == null) {
            return AjaxResult.success(orderService.list());
        } else {
            List<Order> orderList = orderService.list(new QueryWrapper<Order>().eq("customer_id", customerId));
            if (orderList == null || orderList.size() == 0) return AjaxResult.error("该客户没有订单");
            return AjaxResult.success(orderList);
        }
    }

    @ApiOperation("根据订单ID，获取订单详情")
    @GetMapping("/detail/{orderId}/{orderType}")
    public AjaxResult getOrderDetailByOrderId(
            @ApiParam(name = "orderId", value = "订单ID") @PathVariable Long orderId,
            @ApiParam(name = "orderType", value = "订单类型") @PathVariable String orderType) {
        //根据不同的订单信息回显不同的数据
        AjaxResult ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "查询成功");

        if (OperationTypeConstant.ORDER.equals(orderType)) {
            //回显neworder信息以及对应的商品信息
            NewOrder newOrder = newOrderService.getById(orderId);
            if (newOrder == null) return AjaxResult.error("订单不存在");
            ajaxResult.put("order", newOrder);
        } else {
            //回显refund信息以及对应的商品信息
            Refund refund = refundService.getById(orderId);
            if (refund == null) return AjaxResult.error("订单不存在");
            ajaxResult.put("order", refund);
        }
        //获取商品列表回传
        List<Product> productList = productService.list(new QueryWrapper<Product>().eq("order_id", orderId));
        ajaxResult.put("productList", productList);
        return ajaxResult;
    }


    @ApiOperation("修改订单状态")
    @PostMapping("/update/{orderId}/{status}")
    public AjaxResult updateOrderStatus(
            @ApiParam(name = "orderId", value = "订单ID") @PathVariable Long orderId,
            @ApiParam(name = "status", value = "订单状态") @PathVariable String status) {
        //根据订单ID获取订单信息
        Order order = orderService.getById(orderId);
        if (order == null) return AjaxResult.error("订单不存在");
        order.setStatus(status);
        orderService.updateById(order);
        return AjaxResult.success();
    }


    @ApiOperation("根据子站ID查询订单数量")
    @GetMapping("/count/{substationId}")
    public Boolean getOrderCountBySubstationId(@PathVariable Long substationId) {
        long count = newOrderService.count(new QueryWrapper<NewOrder>().eq("substation_id", substationId));
        return count > 0;
    }

    @ApiOperation("批量更新订单状态,远程调用专用")
    @PostMapping("/feign/batchUpdateStatus")
    public Boolean batchUpdateStatus(@RequestParam("status") String status, @RequestBody List<Long> orderIdList) {
        return orderService.batchUpdateStatus(status, orderIdList);
    }




}

