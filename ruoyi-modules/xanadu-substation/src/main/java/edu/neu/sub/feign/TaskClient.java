package edu.neu.sub.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用任务单服务，获取分站对应的任务单
 */


@FeignClient(value = "xanadu-dpc")
public interface TaskClient {
    @PostMapping("/dpc/task/feign/updateTaskStatus/{taskId}/{status}")
    @ApiOperation("根据任务id更新任务状态")
    AjaxResult updateTaskStatus(@PathVariable("taskId") Long taskId, @PathVariable("status") String status);


    @ApiOperation("根据子站ID获取所有的任务列表")
    @GetMapping("/dpc/task/feign/getTaskBySubstationId/{id}")
    public AjaxResult getTaskBySubstationId(@PathVariable("id") Long id);


    @PostMapping("/feign/createDeliveryTask")
    @ApiOperation(value = "创建送货任务单,需要调度商品以及保存相关的商品信息")
    public AjaxResult createDeliveryTask(@ApiParam("订单ID") @RequestParam("orderId") Long orderId,
                                         @ApiParam("分站ID") @RequestParam("substationId") Long substationId,
                                         @RequestParam("subwareId") Long subwareId);

}

