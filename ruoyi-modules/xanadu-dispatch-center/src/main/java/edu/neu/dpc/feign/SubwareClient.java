package edu.neu.dpc.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "xanadu-ware",contextId = "subwareClient")
public interface SubwareClient {
    @GetMapping("/ware/subware/feign/info/{id}")
    @ApiOperation(value = "根据分库ID获取分库信息")
    public AjaxResult info(@PathVariable("id") Long id) ;


}
