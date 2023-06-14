package edu.neu.ware.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "xanadu-dpc", contextId = "taskClient")
public interface TaskClient {
    @GetMapping("/feign/getOrderIdByTaskId/{taskId}")
    @ApiOperation("根据任务id获取订单id")
    Long getOrderIdByTaskId(@PathVariable("taskId") Long taskId);
}
