package edu.neu.dbc.feign;

import edu.neu.dbc.vo.SingleLackRecordVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "xanadu-cc",  contextId = "StockoutClient")
@Component
public interface StockoutClient {


    @GetMapping("/cc/stockout/feign/getAllLackRecord")
    @ApiOperation("获取所有商品缺货记录,feign远程调用专用，前端不要使用该接口")
    public List<SingleLackRecordVo> getAllLackRecord();


    @GetMapping("/cc/stockout/feign/getLackRecord/{productId}")
    @ApiOperation("获取缺货记录,feign远程调用专用，前端不要使用该接口")
    public List<SingleLackRecordVo> getLackRecordById(Long productId);


    @PostMapping("/cc/stockout/feign/updateLackRecordStatusToPurchased")
    @ApiOperation("更新缺货记录状态,feign远程调用专用，前端不要使用该接口,传递的参数id列表")
    public Boolean updateLackRecordStatusToPurchased(@RequestBody List<Long> ids);

}
