package edu.neu.cc.feign;


import edu.neu.cc.vo.ProductRecordsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "xanadu-ware")
public interface WareCenterStorageRecordClient {

    @RequestMapping("/ware/centerStorageRecord/check")
    public ProductRecordsVo check(Map<Long, Integer> productIdMap);
}
