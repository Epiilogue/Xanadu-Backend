package edu.neu.dpc.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "xanadu-sub")
public interface SubstationClient {

    @GetMapping("/sub/substation/feign/getSubwareId/{id}")
    @ApiOperation(value = "获取分站的仓库ID")
    public AjaxResult getSubwareId(@PathVariable("id") Long id) ;

    @GetMapping("/sub/substation/feign/getSubstation/{id}")
    @ApiOperation(value = "获取分站的信息")
    public AjaxResult getSubstation(@PathVariable("id") Long id) ;

    @GetMapping("/sub/substation/feign/getSubstationBySubwareId/{subwareId}")
    @ApiOperation(value = "根据仓库ID获取分站ID")
    public Long getSubstationBySubwareId(@PathVariable("subwareId") Long subwareId);
}
