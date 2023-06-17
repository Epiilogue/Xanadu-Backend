package edu.neu.sub.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "xanadu-cc")
public interface OrderClient {

    @GetMapping("/cc/order/feign/count/{substationId}")
    public Boolean getOrderCountBySubstationId(@PathVariable("substationId") Long substationId);

}
