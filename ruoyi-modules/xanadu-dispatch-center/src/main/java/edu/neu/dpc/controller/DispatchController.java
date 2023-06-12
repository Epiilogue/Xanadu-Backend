package edu.neu.dpc.controller;


import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.dpc.entity.Dispatch;
import edu.neu.dpc.entity.Product;
import edu.neu.dpc.entity.Task;
import edu.neu.dpc.feign.CCOrderClient;
import edu.neu.dpc.feign.CenterWareClient;
import edu.neu.dpc.service.DispatchService;
import edu.neu.dpc.service.ProductService;
import edu.neu.dpc.service.TaskService;
import edu.neu.dpc.vo.DispatchVo;
import edu.neu.dpc.vo.OrderVo;
import edu.neu.dpc.vo.StorageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
@RestController
@RequestMapping("/dpc/dispatch")
@Api(value = "调度中心", tags = "调度中心")
@Transactional
public class DispatchController {

    //1. 列出所有的订单，可以复用order接口，获取所有订单
    //2. 点击检查，可以检查订单状态是否从缺货恢复，如果恢复则可以进行分配，并且锁定库存，远程调用实现

    @Autowired
    CCOrderClient ccOrderClient;

    @Autowired
    TaskService taskService;

    @Autowired
    ProductService productService;

    @Autowired
    DispatchService dispatchService;

    @Autowired
    CenterWareClient centerWareClient;


    @GetMapping("/check/{id}")
    @ApiOperation(value = "检查订单状态，是否全部都到货，如果都到货则更新为可分配状态，需要锁定库存", notes = "检查订单")
    public AjaxResult checkOrder(@PathVariable("id") Long id) {
        return ccOrderClient.checkAllArrival(id);
    }


    /**
     * 任务单： 订单号、任务单、客户姓名、投递地址、商品名称、商品数量、要求完成日期、任务类型、任务状态
     * <p>
     * 商品调拨单： 商品调拨单号、入库库房、商品分类、商品名称、库存量、调拨数量、要求出库日期
     * <p>
     * 还需要考虑是货到付款还是先付款再送货，若是先付款再送货则需要生成的是付款任务单，完成送货后调度为送货任务单
     */


    @PutMapping("/dispatchOrder/{id}/{substationId}")
    @ApiOperation(value = "调度订单,传入参数为订单id和分站id,要求出库日期", notes = "调度订单")
    public AjaxResult dispatchOrder(@ApiParam("订单ID") @PathVariable("id") Long id,
                                    @ApiParam("子站ID") @PathVariable("substationId") Long substationId) {
        //拉取订单信息，生成任务单
        AjaxResult orderResult = ccOrderClient.getOrder(id);
        //检查返回结果是否有错误
        if (orderResult.isError()) return orderResult;

        //获取订单信息
        Object data = orderResult.get("data");
        //转为OrderVo
        OrderVo orderVo = JSON.copyTo(data, OrderVo.class);
        //生成任务单
        Task task = new Task(null, orderVo.getId(), substationId, TaskStatus.ASSIGNED
                , false, orderVo.getOrderType());
        boolean success;
        //1.保存任务
        success = taskService.save(task);
        if (!success) throw new RuntimeException("保存任务失败");

        // 拿到对应的记录ID
        Long taskId = task.getId();
        //2.更新订单状态为已调度
        Boolean isRemoteSuccess = ccOrderClient.batchUpdateStatus(OrderStatusConstant.DISPATCHED, Collections.singletonList(orderVo.getId()));
        if (isRemoteSuccess == null || !isRemoteSuccess) throw new RuntimeException("更新订单状态失败");

        //3.记录任务单对应的商品列表
        List<Product> products = orderVo.getProducts();
        products.forEach(p -> p.setTaskId(taskId));
        success = productService.saveBatch(products);
        if (!success) throw new RuntimeException("保存商品失败");

        //生成调度出库记录，解锁，添加到已分配区
        products.forEach(p -> {
            //修改库存，将对应的商品库存的加锁量减去商品数量，增加已分配量
            Boolean dispatchResult = centerWareClient.unlock(p.getProductId(), p.getNumber());
            if (dispatchResult == null || !dispatchResult) throw new RuntimeException("解锁库存失败");
        });
        return AjaxResult.success("调度成功");
    }


    @PutMapping("/dispatchProduct")
    @ApiOperation(value = "调度商品,传入参数为商品id和分库id,要求出库日期", notes = "调度商品")
    public AjaxResult dispatchProduct(@ApiParam("子库ID") @RequestParam("subwareId") Long subwareId,
                                      @ApiParam("要求出库日期") @RequestParam("requireDate") Date requireDate,
                                      @ApiParam("商品信息") Product product) {
        //构造并保存调度单
        Dispatch dispatch = new Dispatch(null, subwareId, product.getId(), product.getNumber(), product.getProductName(), product.getProductCategary(), requireDate, Dispatch.SUBMITTED);
        boolean success = dispatchService.save(dispatch);
        //尝试修改库存，调度需要从可分配库存中减去对应的数量，添加到已分配库存中，后续从已分配库存中减去对应的数量
        if (!success) throw new RuntimeException("保存调度单失败");
        AjaxResult unlock = centerWareClient.dispatch(product.getId(), product.getNumber(), "unlock");
        if (unlock.isError()) throw new RuntimeException("解锁库存失败");
        return AjaxResult.success("调度成功");
    }


    @GetMapping("/info/{id}")
    @ApiOperation(value = "获取调度单信息", notes = "获取调度单信息")
    public AjaxResult getDispatchInfo(@ApiParam("调度单ID") @PathVariable("id") Long id) {
        Dispatch dispatch = dispatchService.getById(id);
        if (dispatch == null) return AjaxResult.error("调度单不存在");
        //需要查询商品 填充vo
        DispatchVo dispatchVo = new DispatchVo();
        BeanUtils.copyProperties(dispatch, dispatchVo);
        Long productId = dispatch.getProductId();
        //商品ID 查询库存信息
        StorageVo storage = centerWareClient.getStorage(productId);
        if (storage == null) return AjaxResult.error("库存信息不存在");
        BeanUtils.copyProperties(storage, dispatchVo);
        return AjaxResult.success(dispatchVo);
    }




}

