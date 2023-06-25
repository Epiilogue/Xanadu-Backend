package edu.neu.ware.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "xanadu-sub" , contextId = "SubstationClient")
public interface SubstationClient {
    @GetMapping("/sub/substation/feign/getSubstationId/{subwareId}")
    @ApiOperation(value = "获取分站的ID")
    public AjaxResult getSubstationId(@PathVariable("subwareId") Long subwareId);


    @GetMapping("/sub/substation/feign/getSubwareId/{id}")
    @ApiOperation(value = "获取分站的仓库ID")
    public AjaxResult getSubwareId(@PathVariable("id") Long id);
}
