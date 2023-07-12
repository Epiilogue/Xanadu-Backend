package edu.neu.ware.feign;

import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "xanadu-dpc", contextId = "taskClient")
public interface TaskClient {
    @GetMapping("/dpc/task/feign/getOrderIdByTaskId/{taskId}")
    @ApiOperation("根据任务id获取订单id")
    Long getOrderIdByTaskId(@PathVariable("taskId") Long taskId);

    @PostMapping("/dpc/task/feign/updateTaskStatus/{taskId}/{status}")
    @ApiOperation("根据任务id更新任务状态")
    AjaxResult updateTaskStatus(@PathVariable("taskId") Long taskId, @PathVariable("status") String status);

    @PutMapping("/dpc/dispatch/feign/updateDispatchStatus/{id}")
    @ApiOperation("更新调度单状态为已出库,传入参数为调度单id和调度单信息")
    public AjaxResult updateDispatchStatus(@ApiParam("调度单ID") @PathVariable("id") Long id);
}
