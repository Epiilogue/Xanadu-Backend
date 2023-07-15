package edu.neu.sub.feign;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "xanadu-ac", contextId = "InvoicesClient")
public interface InvoicesClient {

    @ApiOperation("获取已经领用过发票的订单列表,feign调用")
    @PostMapping("/ac/invoices/feign/check")
    public List<Long> check(@RequestBody List<Long> orderIds);

}
