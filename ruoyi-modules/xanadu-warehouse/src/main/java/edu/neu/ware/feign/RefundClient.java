package edu.neu.ware.feign;

import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "xanadu-dbc", contextId = "RefundClient")
public interface RefundClient {

    @GetMapping("/dbc/refund/feign/getRefundId/{refundId}")
    AjaxResult updateRefundStatus(@PathVariable("refundId") Long refundId);
}
