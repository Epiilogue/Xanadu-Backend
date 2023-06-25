package edu.neu.ware.feign;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "xanadu-dbc", contextId = "PurchaseRecordClient")
public interface PurchaseRecordClient {

    @ApiOperation("获取缺货记录ID列表,feign调用")
    @GetMapping("/dbc/purchaseRecord/feign/getLackIds/{purchaseId}}")
    public List<Long> getLackIds(@PathVariable("purchaseId") Long purchaseId);

}
