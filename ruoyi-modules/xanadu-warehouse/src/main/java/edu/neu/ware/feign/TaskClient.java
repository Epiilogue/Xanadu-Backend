package edu.neu.ware.feign;

import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "xanadu-dpc", contextId = "taskClient")
public interface TaskClient {
    @GetMapping("/dpc/task/feign/getOrderIdByTaskId/{taskId}")
    @ApiOperation("根据任务id获取订单id")
    Long getOrderIdByTaskId(@PathVariable("taskId") Long taskId);

    @PostMapping("/dpc/task/feign/updateTaskStatus/{taskId}/{status}")
    @ApiOperation("根据任务id更新任务状态")
    AjaxResult updateTaskStatus(@PathVariable("taskId") Long taskId, @PathVariable("status") String status);
}
