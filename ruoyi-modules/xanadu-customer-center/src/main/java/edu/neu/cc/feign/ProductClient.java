package edu.neu.cc.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.cc.vo.ProductRecordsVo;
import edu.neu.cc.vo.ProductSimpleVo;
import edu.neu.cc.vo.ProductVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "xanadu-dbc", contextId = "productClient")
public interface ProductClient {

    @GetMapping("/dbc/product/feign/remoteGet/{id}")
    @ApiOperation("获取商品")
    public ProductSimpleVo remoteGet(@PathVariable("id") Long id);

}
