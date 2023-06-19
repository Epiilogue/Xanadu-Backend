package edu.neu.sub.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@FeignClient(value = "xanadu-ware")
public interface SubwareClient {


    @PostMapping("/ware/subStorageRecord/feign/check")
    @ApiOperation(value = "检查商品是否数量都充足")
    AjaxResult check(@RequestParam("subwareId") Long subwareId,@RequestBody HashMap<Long, Integer> longIntegerHashMap);
    @PostMapping("/ware/subStorageRecord/feign/lock")
    @ApiOperation(value = "锁定库存")
    AjaxResult lock(@RequestParam("subwareId") Long subwareId,@RequestBody HashMap<Long, Integer> longIntegerHashMap);

    @PostMapping("/ware/subStorageRecord/feign/reduce")
    @ApiOperation(value = "减少库存")
    AjaxResult reduce(@RequestParam("subwareId") Long subwareId, @RequestParam("taskId") Long taskId, @RequestBody HashMap<Long, Integer> longIntegerHashMap);
}
