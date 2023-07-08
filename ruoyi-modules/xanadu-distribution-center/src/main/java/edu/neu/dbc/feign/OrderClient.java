package edu.neu.dbc.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "xanadu-cc",contextId = "OrderClient")

public interface OrderClient {
    @PostMapping("/cc/order/feign/batchUpdateStatus")
    public Boolean batchUpdateStatus(@RequestParam("status") String status, @RequestBody List<Long> orderIdList);
    @GetMapping("/cc/order/feign/checkDeleteProduct/{id}")
    AjaxResult checkDeleteProduct(@PathVariable("id") Integer id);
}
