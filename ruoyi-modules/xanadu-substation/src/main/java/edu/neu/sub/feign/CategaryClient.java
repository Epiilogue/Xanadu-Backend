package edu.neu.sub.feign;

import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 分类服务接口，调用远程方法
 */
@FeignClient(value = "xanadu-dbc")
public interface CategaryClient {

    @ApiOperation("根据id获取小分类下的所有商品")
    @GetMapping("/dbc/categary/feign/getGoods/{id}")
    public AjaxResult getGoods(@PathVariable("id") Integer id);
}
