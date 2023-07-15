package edu.neu.cc.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.cc.entity.*;
import edu.neu.cc.service.*;
import edu.neu.cc.vo.OrderVo;
import edu.neu.cc.vo.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/order")
@CacheConfig(cacheNames = "order")
@Transactional(rollbackFor = Exception.class)
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

    @ApiOperation("获取已完成订单列表,数据大屏使用")
    @GetMapping("/listAll")
    public AjaxResult listAll() {
        List<Order> orderList = orderService.list();
        List<Order> completed = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getStatus().equals(OrderStatusConstant.COMPLETED))
                completed.add(order);
        }
        return AjaxResult.success(completed);
    }


    @ApiOperation("根据客户ID，获取订单列表，如果客户ID为空，则获取所有订单列表")
    @GetMapping(value = {"/list/{customerId}", "/list"})
    public AjaxResult getOrderListByCustomerId(@PathVariable(required = false) Long customerId) {
        if (customerId == null) {
            return AjaxResult.success(orderService.list());
        } else {
            List<Order> orderList = orderService.list(new QueryWrapper<Order>().eq("customer_id", customerId));
            if (orderList == null || orderList.size() == 0) return AjaxResult.error("该客户没有订单");
            return AjaxResult.success(orderList);
        }
    }

    @ApiOperation("根据客户ID，获取订单列表，如果客户ID为空，则获取所有订单列表")
//    @GetMapping(value={"/list/{customerId}","/list"})
    @CrossOrigin
    public AjaxResult getOrderListByCustomerId(@PathVariable(required = false) Long customerId, @RequestParam Map<String, String> query) {
        //设置查询条件
        Date startTime = DateUtil.parse(query.get("beginTime")); //起始时间
        Date endTime = DateUtil.parse(query.get("endTime"));    //结束时间
        String customerName = (String) query.get("customerName");
        String orderType = (String) query.get("orderType");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like(customerName != null, "customer_name", customerName)
                .ge(startTime != null, "create_time", startTime)
                .le(endTime != null, "create_time", endTime)
                .eq(orderType != null && orderType != "全部", "order_type", orderType)
                .eq(customerId != null, "customer_id", customerId);
        //不分页
        if (query.get("page") == null || query.get("limit") == null) {
            return AjaxResult.success(orderService.list(queryWrapper));
            //分页
        } else {
            Long page = Long.parseLong(query.get("page"));
            Long size = Long.parseLong(query.get("limit"));
            Page<Order> orderList = orderService.page(new Page<>(page, size), queryWrapper);
            if (orderList.getRecords() == null || orderList.getRecords().size() == 0)
                return AjaxResult.error("没有满足条件的订单");
            return AjaxResult.success(orderList);
        }
    }

    @ApiOperation("根据订单ID，获取订单详情")
    @GetMapping(path = {"/detail/{orderId}/{orderType}", "detail/{orderId}"})
    @Cacheable(key = "#orderId")
    public AjaxResult getOrderDetailByOrderId(
            @ApiParam(name = "orderId", value = "订单ID") @PathVariable("orderId") Long orderId,
            @ApiParam(name = "orderType", value = "订单类型") @PathVariable(value = "orderType",required = false) String orderType) {
        //根据不同的订单信息回显不同的数据
        AjaxResult ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "查询成功");

        Order order = orderService.getById(orderId);
        if (order == null) return AjaxResult.error("订单不存在");
        orderType = order.getOrderType();

        if (OperationTypeConstant.ORDER.equals(orderType)) {
            //回显neworder信息以及对应的商品信息
            NewOrder newOrder = newOrderService.getById(orderId);
            if (newOrder == null) return AjaxResult.error("订单不存在");
            ajaxResult.put("origin", order);
            ajaxResult.put("order", newOrder);
        } else {
            //回显refund信息以及对应的商品信息
            Refund refund = refundService.getById(orderId);
            if (refund == null) return AjaxResult.error("订单不存在");
            ajaxResult.put("origin", order);
            ajaxResult.put("order", refund);
        }
        //获取商品列表回传
        List<Product> productList = productService.list(new QueryWrapper<Product>().eq("order_id", orderId));
        ajaxResult.put("productList", productList);
        return ajaxResult;
    }


    @ApiOperation("修改订单状态")
    @PostMapping("/update/{orderId}/{status}")
    @CacheEvict(key = "#orderId")
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
        return newCount > 0;
    }


    @ApiOperation("批量更新订单状态,远程调用专用")
    @PostMapping("/feign/batchUpdateStatus")
    @CacheEvict(allEntries = true)
    public Boolean batchUpdateStatus(@RequestParam("status") String status, @RequestBody List<Long> orderIdList) {
        return orderService.batchUpdateStatus(status, orderIdList);
    }


    @ApiOperation("根据订单ID检查订单是否可从缺货到货")
    @GetMapping("/feign/checkAllArrival/{id}")
    @CacheEvict(key = "#id")
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
        //仅仅切换订单状态即可，不需要加锁，入库的时候会自己加锁，出库的时候一定要保证按照入库的数量减少，避免出现加锁未分配
        order.setStatus(OrderStatusConstant.CAN_BE_ALLOCATED);
        //更新订单状态
        boolean b = orderService.updateById(order);
        if (!b) return AjaxResult.error("更新订单状态失败");
        return AjaxResult.success("订单状态更新成功,订单可分配");
    }


    @ApiOperation("分类统计每月订单数量")
    @GetMapping("/getMonthlyOrdersByType")
    public AjaxResult getMonthlyOrdersByType() {
        List<Order> orderList = orderService.list();
        Map<String, Object> orderMap = new HashMap<>();
        int[] colListOrder = new int[13];
        int[] colListExchange = new int[13];
        int[] colListReturn = new int[13];
        Arrays.fill(colListOrder, 0);
        Arrays.fill(colListReturn, 0);
        Arrays.fill(colListExchange, 0);
        for (Order order : orderList) {
            switch (order.getOrderType()) {
                case OperationTypeConstant.ORDER:
                    int month = DateUtil.month(order.getCreateTime()) + 1;
                    System.out.println(month);
                    colListOrder[month]++;
                    break;
                case OperationTypeConstant.EXCHANGE:
                    month = DateUtil.month(order.getCreateTime()) + 1;
                    colListExchange[month]++;
                    break;
                case OperationTypeConstant.RETURN:
                    month = DateUtil.month(order.getCreateTime()) + 1;
                    colListReturn[month]++;
                    break;
            }
        }
        orderMap.put(OperationTypeConstant.ORDER, colListOrder);
        orderMap.put(OperationTypeConstant.EXCHANGE, colListExchange);
        orderMap.put(OperationTypeConstant.RETURN, colListReturn);
        return AjaxResult.success(orderMap);
    }


    @ApiOperation("根据订单ID获取订单信息")
    @GetMapping("/feign/getOrder/{id}")
    public AjaxResult getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.getById(id);
        if (order == null) return AjaxResult.error("订单不存在");
        //根据订单类型获取对应的订单信息
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);
        //根据订单类型获取对应的订单信息
        switch (order.getOrderType()) {
            case OperationTypeConstant.ORDER:
                NewOrder newOrder = newOrderService.getById(id);
                BeanUtils.copyProperties(newOrder, orderVo);
                orderVo.setNeedInvoice(newOrder.getInvoiceNeed() > 0);
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
                orderVo.setId(id);
                orderVo.setReceiverName(prevOrder.getReceiverName());
                List<Product> refundProducts = productService.list(new QueryWrapper<Product>().eq("order_id", id));
                orderVo.setProducts(refundProducts);
                return AjaxResult.success(orderVo);
            default:
                return AjaxResult.error("订单类型错误");
        }
    }

    @PostMapping("/feign/updateOrderSubstationId/{substationId}/{orderId}")
    @CacheEvict(key = "#orderId")
    public Boolean updateOrderSubstationId(@PathVariable("substationId") Long substationId, @PathVariable("orderId") Long orderId) {
        NewOrder newOrder = newOrderService.getById(orderId);
        if (newOrder == null) return false;
        newOrder.setSubstationId(substationId);
        return newOrderService.updateById(newOrder);
    }


    @GetMapping("/feign/listTopProduct/{number}")
    @Cacheable
    public AjaxResult listTopProducts(@RequestParam("startTime") Date startTime,
                                      @RequestParam("endTime") Date endTime,
                                      @RequestParam("number") Integer number) {

        List<Order> orders = orderService.list(new QueryWrapper<Order>().between("create_time", startTime, endTime).eq("order_type", OperationTypeConstant.ORDER));
        //查询一段时间范围内订购数量最多的前number个商品，返回商品列表，里面需要有商品数量
        //直接去找范围内的新订单，然后找到里面所有的订单ID，然后取商品表查找，最后统计以productVo列表返回
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        Map<Long, ProductVo> longProductVoHashMap = new HashMap<>();
        List<Product> products = productService.list(new QueryWrapper<Product>().in("order_id", orderIds));
        products.forEach(product -> {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(product, productVo);
            if (longProductVoHashMap.containsKey(product.getProductId())) {
                ProductVo tmp = longProductVoHashMap.get(product.getProductId());
                tmp.setNumber(tmp.getNumber() + product.getNumber());
            } else {
                longProductVoHashMap.put(product.getProductId(), productVo);
            }
        });
        return AjaxResult.success(longProductVoHashMap.values().stream().sorted(Comparator.comparing(ProductVo::getNumber).reversed()).limit(number).collect(Collectors.toList()));
    }


    @GetMapping("/feign/checkDeleteProduct/{id}")
    AjaxResult checkDeleteProduct(@PathVariable("id") Integer id) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>().eq("product_id", id);
        long count = productService.count(queryWrapper);
        if (count > 0) return AjaxResult.error("该商品已被下单，无法删除");
        return AjaxResult.success();
    }

}

