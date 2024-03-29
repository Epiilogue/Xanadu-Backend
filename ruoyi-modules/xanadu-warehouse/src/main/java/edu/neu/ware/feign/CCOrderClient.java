package edu.neu.ware.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "xanadu-cc" ,contextId = "CCOrderClient")
public interface CCOrderClient {
    @GetMapping("/cc/order/feign/count/{substationId}")
    public Boolean getOrderCountBySubstationId(@PathVariable("substationId") Long substationId);


    @PostMapping("/cc/stockout/feign/updateLackRecordStatusToArrival")
    @ApiOperation("更新缺货记录状态,feign远程调用专用，前端不要使用该接口,传递的参数id列表,实际到货数量")
    public Boolean updateLackRecordStatusToArrival(@RequestParam("number") Integer number, @RequestBody List<Long> ids);


    @ApiOperation("批量更新订单状态,远程调用专用")
    @PostMapping("/cc/order/feign/batchUpdateStatus")
    public Boolean batchUpdateStatus(@RequestParam("status") String status, @RequestBody List<Long> orderIdList);

}
