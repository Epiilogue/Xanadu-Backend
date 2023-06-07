package edu.neu.dbc.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "xanadu-order-center", url = "/cc/order")
public interface OrderClient {
    @PostMapping("/feign/batchUpdateStatus")
    public Boolean batchUpdateStatus(@RequestParam String status, @RequestBody List<Long> orderIdList);

}
