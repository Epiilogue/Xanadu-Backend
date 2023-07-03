package edu.neu.dpc.controller;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.dpc.entity.Task;
import edu.neu.dpc.feign.CCOrderClient;
import edu.neu.dpc.service.TaskService;
import edu.neu.dpc.vo.OrderVo;
import edu.neu.dpc.vo.TaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
@RequestMapping("/dpc/task")
@CacheConfig(cacheNames = "task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @Autowired
    CCOrderClient ccOrderClient;


    @GetMapping("/feign/getOrderIdByTaskId/{taskId}")
    @ApiOperation("根据任务id获取订单id")
    @Cacheable(key = "#taskId")
    public Long getOrderIdByTaskId(@PathVariable("taskId") Long taskId) {
        Task task = taskService.getById(taskId);
        if (task != null) {
            return task.getOrderId();
        }
        return null;
    }

    @PostMapping("/feign/updateTaskStatus/{taskId}/{status}")
    @ApiOperation("根据任务id更新任务状态")
    @CacheEvict(key = "#taskId")
    public  AjaxResult updateTaskStatus(@PathVariable("taskId") Long taskId, @PathVariable("status") String status) {
        Task task = taskService.getById(taskId);
        if (task != null) {
            task.setTaskStatus(status);
            Long orderId = task.getOrderId();
            Boolean success = false;
            switch (status) {
                case TaskStatus.ASSIGNABLE:
                    //远程调用更新订单状态为配送站到货
                    success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.SUBSTATION_ARRIVAL, Collections.singletonList(orderId));
                    break;
                case TaskStatus.ASSIGNED:
                    success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.ALLOCATED, Collections.singletonList(orderId));
                    break;
                case TaskStatus.RECEIVED:
                    success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.RECEIVED, Collections.singletonList(orderId));
                    break;
                case TaskStatus.FAILED:
                    success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.FAILED, Collections.singletonList(orderId));
                    break;
                case TaskStatus.COMPLETED:
                    //需要判断订单类型，如果是收款订单就没有必要更新订单状态了
                    if (task.getTaskType().equals(TaskType.PAYMENT)) break;
                    success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.FINISHED, Collections.singletonList(orderId));
                    break;
                case TaskStatus.PARTIAL_COMPLETED:
                    success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.PARTIAL_COMPLETED, Collections.singletonList(orderId));
                    break;
                default:
                    return AjaxResult.error("更新任务单状态失败,未知任务单状态");
            }
            if (!success) return AjaxResult.error("更新远程订单状态失败");
            taskService.updateById(task);
        }
        return AjaxResult.success("更新任务单状态成功");
    }


    /**
     * 任务号、客户姓名、投递地址、商品名称、商品数量、要求完成日期、任务类型、任务状态。
     * 说明：
     */
    @ApiOperation("根据子站ID获取所有的任务列表")
    @GetMapping("/feign/getTaskBySubstationId/{id}")
    public AjaxResult getTaskBySubstationId(@PathVariable("id") Long id) {
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<Task>().eq("sub_id", id);
        //首先需要拿到所有的任务，然后对于每一个任务，都要反查订单信息，拼接为vo返回
        List<Task> list = taskService.list(taskQueryWrapper);
        if (list == null || list.size() == 0) return AjaxResult.error("查询子站任务失败");
        ArrayList<TaskVo> taskVos = new ArrayList<>();
        list.forEach(t -> {
            Long orderId = t.getOrderId();
            AjaxResult orderResult = ccOrderClient.getOrder(orderId);
            //检查返回结果是否有错误,如果有错误则不生成vo
            if (orderResult.isError()) return;
            //获取订单信息
            Object data = orderResult.get("data");
            //转为OrderVo
            OrderVo orderVo = JSON.parseObject(JSON.toJSONString(data), OrderVo.class);
            //拼接为TaskVo
            TaskVo taskVo = new TaskVo();
            BeanUtils.copyProperties(orderVo, taskVo);
            BeanUtils.copyProperties(t, taskVo);
            if(taskVo.getNeedInvoice()==null) taskVo.setNeedInvoice(false);
            taskVos.add(taskVo);
        });
        return AjaxResult.success(taskVos);
    }


}

