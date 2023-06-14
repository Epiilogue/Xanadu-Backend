package edu.neu.dpc.controller;


import edu.neu.dpc.entity.Task;
import edu.neu.dpc.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/feign/getOrderIdByTaskId/{taskId}")
    @ApiOperation("根据任务id获取订单id")
    Long getOrderIdByTaskId(@PathVariable("taskId") Long taskId) {
        Task task = taskService.getById(taskId);
        if (task != null) {
            return task.getOrderId();
        }
        return null;
    }

}

