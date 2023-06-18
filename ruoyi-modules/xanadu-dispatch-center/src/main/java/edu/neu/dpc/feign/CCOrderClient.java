package edu.neu.dpc.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dpc.vo.OrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "xanadu-cc", contextId = "CCOrderClient")
public interface CCOrderClient {


    @GetMapping("/cc/order/feign/checkAllArrival/{id}")
    AjaxResult checkAllArrival(@PathVariable("id") Long id);


    @GetMapping("/cc/order/feign/getOrder/{id}")
    AjaxResult getOrder(@PathVariable("id") Long id);

    @PostMapping("/cc/order/feign/batchUpdateStatus")
    public Boolean batchUpdateStatus(@RequestParam("status") String status, @RequestBody List<Long> orderIdList);

    @PostMapping("/cc/order/feign/updateOrderSubstationId/{substationId}/{orderId}")
    public  Boolean updateOrderSubstationId(@PathVariable("substationId") Long substationId, @PathVariable("orderId") Long orderId);


}
