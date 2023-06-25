package edu.neu.ac.feign;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.ac.vo.SettlementVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient("xanadu-dbc")
public interface SettleClient {

    @GetMapping("/dbc/purchaseRecord/feign/settlement")
    public AjaxResult settlement(@RequestParam(value = "supplierId") Long supplierId, @RequestParam(value = "productId") Long productId,
                                 @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime);


    @PostMapping("/dbc/purchaseRecord/feign/updateStatus")
    AjaxResult updateStatus(@RequestBody  List<SettlementVo> settlementVos);
}
