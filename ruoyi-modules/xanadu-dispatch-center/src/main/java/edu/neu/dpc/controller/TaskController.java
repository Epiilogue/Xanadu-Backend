package edu.neu.dpc.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.OrderStatusConstant;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.dpc.entity.Task;
import edu.neu.dpc.feign.CCOrderClient;
import edu.neu.dpc.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

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
public class TaskController {
    @Autowired
    TaskService taskService;

    @Autowired
    CCOrderClient ccOrderClient;


    @GetMapping("/feign/getOrderIdByTaskId/{taskId}")
    @ApiOperation("根据任务id获取订单id")
    Long getOrderIdByTaskId(@PathVariable("taskId") Long taskId) {
        Task task = taskService.getById(taskId);
        if (task != null) {
            return task.getOrderId();
        }
        return null;
    }

    @PostMapping("/feign/updateTaskStatus/{taskId}/{status}")
    @ApiOperation("根据任务id更新任务状态")
    AjaxResult updateTaskStatus(@PathVariable("taskId") Long taskId, @PathVariable("status") String status) {
        Task task = taskService.getById(taskId);
        if (task != null) {
            task.setTaskStatus(status);
            switch (status) {
                case TaskStatus.ASSIGNABLE:
                    //远程调用更新订单状态为配送站到货
                    Long orderId = task.getOrderId();
                    Boolean success = ccOrderClient.batchUpdateStatus(OrderStatusConstant.SUBSTATION_ARRIVAL, Collections.singletonList(orderId));
                    if (!success) return AjaxResult.error("更新订单状态失败,订单不存在");
                    break;
                default:
                    break;
            }
            taskService.updateById(task);
        }
        return AjaxResult.error("更新任务单状态失败,任务不存在");
    }


}

