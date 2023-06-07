package edu.neu.dbc.feign;

import edu.neu.dbc.vo.SingleLackRecordVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "xanadu-cc", url = "/cc/stockout",contextId = "dbc-cc-2")
@Component
public interface StockoutClient {


    @GetMapping("/feign/getAllLackRecord")
    @ApiOperation("获取所有商品缺货记录,feign远程调用专用，前端不要使用该接口")
    public List<SingleLackRecordVo> getAllLackRecord();


    @GetMapping("/feign/getLackRecord/{productId}")
    @ApiOperation("获取缺货记录,feign远程调用专用，前端不要使用该接口")
    public List<SingleLackRecordVo> getLackRecordById(Long productId);


}
