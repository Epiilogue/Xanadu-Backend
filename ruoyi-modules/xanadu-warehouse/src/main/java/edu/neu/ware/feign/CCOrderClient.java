package edu.neu.ware.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "xanadu-cc")
public interface CCOrderClient {
    @GetMapping("/count/{substationId}")
    public Boolean getOrderCountBySubstationId(@PathVariable Long substationId);
}
