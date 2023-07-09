package edu.neu.dbc.feign;



import edu.neu.dbc.vo.StorageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value = "xanadu-ware",contextId = "WareCenterStorageRecordClient")
@Component
public interface WareCenterStorageRecordClient {
    @GetMapping("/ware/centerStorageRecord/feign/getStorage/{productId}")
    public StorageVo getStorage(@PathVariable("productId") Long productId);
}
