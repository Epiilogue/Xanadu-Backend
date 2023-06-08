package edu.neu.cc.feign;


import edu.neu.cc.vo.ProductRecordsVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "xanadu-ware", contextId = "WareCenterStorageRecordClient")
public interface WareCenterStorageRecordClient {

    @RequestMapping("/ware/centerStorageRecord/check")
    public ProductRecordsVo check(Map<Long, Integer> productIdMap);


    @PutMapping("/feign/unlock/{productId}/{num}")
    @ApiOperation("解锁商品库存")
    public Boolean unlock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num);

    @PutMapping("/feign/lock/{productId}/{num}")
    @ApiOperation("锁定商品库存")
    public Boolean lock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num);

}
