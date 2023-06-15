package edu.neu.ware.feign;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(value = "xanadu-dbc", contextId = "SupplierClient")
public interface SupplierClient {

    @GetMapping("/dbc/supplier/feign/getSupplierNames")
    @ApiOperation("获取所有供应商的id和名字")
    public Map<Long, String> getSupplierNames();
}
