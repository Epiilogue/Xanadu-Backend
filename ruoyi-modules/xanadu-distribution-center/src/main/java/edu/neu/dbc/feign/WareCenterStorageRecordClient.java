package edu.neu.dbc.feign;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value = "xanadu-ware", url = "/ware/centerStorageRecord")
public interface WareCenterStorageRecordClient {
    @GetMapping("/feign/getStorage/{productId}")
    public Integer getStorage(@PathVariable("productId") Long productId);
}
