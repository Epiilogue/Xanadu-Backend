package edu.neu.cc.controller;


import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.cc.entity.*;
import edu.neu.cc.service.*;
import edu.neu.cc.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/order")
@CrossOrigin
public class OrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private NewOrderService newOrderService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StockoutService stockoutService;

    @ApiOperation("根据客户ID，获取订单列表，如果客户ID为空，则获取所有订单列表")
    @GetMapping("/list/{customerId}")
    @CrossOrigin
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
    @GetMapping("/feign/count/{substationId}")
    public Boolean getOrderCountBySubstationId(@PathVariable Long substationId) {
        long newCount = newOrderService.count(new QueryWrapper<NewOrder>().eq("substation_id", substationId));
        long refundCount = refundService.count(new QueryWrapper<Refund>().eq("substation_id", substationId));
        return newCount + refundCount > 0;
    }


    @ApiOperation("批量更新订单状态,远程调用专用")
    @PostMapping("/feign/batchUpdateStatus")
    public Boolean batchUpdateStatus(@RequestParam("status") String status, @RequestBody List<Long> orderIdList) {
        return orderService.batchUpdateStatus(status, orderIdList);
    }


    @ApiOperation("根据订单ID检查订单是否可从缺货到货")
    @GetMapping("/feign/checkAllArrival/{id}")
    public AjaxResult checkAllArrival(@PathVariable("id") Long id) {
        //先校验，检查订单是否存在
        Order order = orderService.getById(id);
        if (order == null) return AjaxResult.error("订单不存在");
        //检查订单状态是否为缺货
        if (!OrderStatusConstant.OUT_OF_STOCK.equals(order.getStatus())) return AjaxResult.error("订单状态非缺货状态");
        //获取订单的所有缺货记录
        List<Stockout> stockouts = stockoutService.list(new QueryWrapper<Stockout>().eq("order_id", id));
        //检查订单的所有缺货记录是否都已经到货
        for (Stockout stockout : stockouts) {
            if (!stockout.getStatus().equals(StockoutConstant.ARRIVAL))
                return AjaxResult.error("订单中存在未到货的商品");
        }
        //TODO: 修改订单状态为可分配，我们需要提前锁定库存，避免其他的订单把这部分库存划走
        order.setStatus(OrderStatusConstant.CAN_BE_ALLOCATED);
        //更新订单状态
        boolean b = orderService.updateById(order);
        if (!b) return AjaxResult.error("更新订单状态失败");
        return AjaxResult.success("订单状态更新成功,订单可分配");
    }


    @ApiOperation("根据订单ID获取订单信息")
    @GetMapping("/feign/getOrder/{id}")
    public AjaxResult getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.getById(id);
        if (order == null) return AjaxResult.error("订单不存在");
        if (order.getOrderType().equals(OperationTypeConstant.UNSUBSCRIBE))
            return AjaxResult.error("该订单为退订订单,无法调度");
        //检查订单状态是否为可分配
        if (!order.getStatus().equals(OrderStatusConstant.CAN_BE_ALLOCATED))
            return AjaxResult.error("订单状态非可分配状态");

        //根据订单类型获取对应的订单信息
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        //根据订单类型获取对应的订单信息
        switch (order.getOrderType()) {
            case OperationTypeConstant.ORDER:
                NewOrder newOrder = newOrderService.getById(id);
                BeanUtils.copyProperties(newOrder, orderVo);
                orderVo.setReceiverName(newOrder.getReceiverName());
                orderVo.setPhone(newOrder.getTelephone());
                //如果是新订单，则获取商品列表后返回
                List<Product> productList = productService.list(new QueryWrapper<Product>().eq("order_id", id));
                orderVo.setProducts(productList);
                return AjaxResult.success(orderVo);
            case OperationTypeConstant.EXCHANGE:
            case OperationTypeConstant.RETURN:
                //需要获取refund，然后反查neworder，然后获取商品列表
                Refund refund = refundService.getById(id);
                if (refund == null) return AjaxResult.error("订单不存在");
                //拿到原始的订单id
                Long newOrderId = refund.getOrderId();
                NewOrder prevOrder = newOrderService.getById(newOrderId);
                if (prevOrder == null) return AjaxResult.error("原始订单不存在");
                BeanUtils.copyProperties(prevOrder, orderVo);
                orderVo.setReceiverName(prevOrder.getReceiverName());
                List<Product> refundProducts = productService.list(new QueryWrapper<Product>().eq("order_id", id));
                orderVo.setProducts(refundProducts);
                return AjaxResult.success(orderVo);
            default:
                return AjaxResult.error("订单类型错误");
        }
    }

    @PostMapping("/cc/order/feign/updateOrderSubstationId/{substationId}/{orderId}")
    public Boolean updateOrderSubstationId(@PathVariable("substationId") Long substationId, @PathVariable("orderId") Long orderId) {
        NewOrder newOrder = newOrderService.getById(orderId);
        if (newOrder == null) return false;
        newOrder.setSubstationId(substationId);
        return newOrderService.updateById(newOrder);
    }


}

