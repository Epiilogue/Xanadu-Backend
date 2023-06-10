package edu.neu.dpc.controller;


import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.dpc.entity.Task;
import edu.neu.dpc.feign.CCOrderClient;
import edu.neu.dpc.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
public class DispatchController {

    //1. 列出所有的订单，可以复用order接口，获取所有订单
    //2. 点击检查，可以检查订单状态是否从缺货恢复，如果恢复则可以进行分配，并且锁定库存，远程调用实现

    @Autowired
    CCOrderClient ccOrderClient;

    @Autowired



    @GetMapping("/check/{id}")
    @ApiOperation(value = "检查订单状态，是否全部都到货，如果都到货则更新为可分配状态，需要锁定库存", notes = "检查订单")
    public AjaxResult checkOrder(@PathVariable("id") Long id) {
        return ccOrderClient.checkAllArrival(id);
    }



    /**
     * 任务单： 订单号、任务单、客户姓名、投递地址、商品名称、商品数量、要求完成日期、任务类型、任务状态
     * <p>
     * 商品调拨单： 商品调拨单号、出库库房、入库库房、商品分类、商品名称、库存量、调拨数量、计量单位、要求出库日期
     */


    @PutMapping("/dispatch/{id}/{substationId}")
    @ApiOperation(value = "调度订单,传入参数为订单id和分站id,要求出库日期", notes = "调度订单")
    public AjaxResult dispatchOrder(@PathVariable("id") Long id, @PathVariable("substationId") Long substationId) {
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



        return null;
    }


}

