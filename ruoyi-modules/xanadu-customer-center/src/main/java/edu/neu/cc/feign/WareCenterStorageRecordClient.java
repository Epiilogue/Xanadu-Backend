package edu.neu.cc.feign;


import edu.neu.cc.vo.ProductRecordsVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "xanadu-ware", contextId = "WareCenterStorageRecordClient")
public interface WareCenterStorageRecordClient {

    @PostMapping ("/ware/centerStorageRecord/feign/check")
    public ProductRecordsVo check(@RequestBody Map<Long, Integer> productIdMap);


    @PutMapping("/ware/centerStorageRecord/feign/unlock/{productId}/{num}")
    @ApiOperation("解锁商品库存")
    public Boolean unlock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num);

    @PutMapping("/ware/centerStorageRecord/feign/lock/{productId}/{num}")
    @ApiOperation("锁定商品库存")
    public Boolean lock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num);

}
