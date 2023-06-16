package edu.neu.sub.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "xanadu-cc")
public interface OrderClient {

    @GetMapping("/cc/order/feign/count/{substationId}")
    public Boolean getOrderCountBySubstationId(@PathVariable("substationId") Long substationId);


}
