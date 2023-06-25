package edu.neu.ac.controller;


import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.ac.entity.Supply;
import edu.neu.ac.feign.SettleClient;
import edu.neu.ac.service.SupplyService;
import edu.neu.ac.vo.SettlementVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-23 09:35:39
 */
@RestController
@RequestMapping("/ac/supply")
@Transactional
public class SupplyController {


    @Autowired
    SettleClient settleClient;

    @Autowired
    SupplyService supplyService;


    @GetMapping("/listToSettlement")
    @ApiOperation("与供应商结算,获取结算列表")
    public AjaxResult settlement(@RequestParam(value = "supplierId") Long supplierId, @RequestParam(value = "productId") Long productId,
                                 @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        AjaxResult ajaxResult = settleClient.settlement(supplierId, productId, startTime, endTime);
        if (ajaxResult == null) return AjaxResult.error("获取结算订单失败");
        if (ajaxResult.isError()) return AjaxResult.error(ajaxResult.getMsg());
        List<SettlementVo> settlementVos = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SettlementVo.class);
        if (settlementVos == null || settlementVos.size() == 0) return AjaxResult.error("获取结算订单失败");
        return AjaxResult.success(settlementVos);
    }

    @GetMapping("/listSettlement")
    @ApiOperation("查看结算记录,前端分页筛选查找")
    public AjaxResult listSettlement() {
        return AjaxResult.success(supplyService.list());
    }


    @DeleteMapping("/deleteSettlement/{id}")
    @ApiOperation("删除结算记录")
    public AjaxResult deleteSettlement(@PathVariable Long id) {
        boolean remove = supplyService.removeById(id);
        if (!remove) return AjaxResult.error("删除结算记录失败");
        return AjaxResult.success("删除结算记录成功");
    }


    @PostMapping("/postSettlement")
    @ApiOperation("与供应商结算,提交结算")
    public AjaxResult postSettlement(@RequestBody List<SettlementVo> settlementVos) {
        if (settlementVos == null || settlementVos.size() == 0) return AjaxResult.error("提交结算失败");
        //提交结算
        List<Supply> supplies = settlementVos.stream().map(settlementVo -> {
            Supply supply = new Supply();
            supply.setDeleted(false);
            BeanUtils.copyProperties(settlementVo, supply);
            supply.setTime(new Date());
            return supply;
        }).collect(Collectors.toList());
        //保存结算
        boolean save = supplyService.saveBatch(supplies);
        if (!save) return AjaxResult.error("提交结算订单失败");
        //根据结算的信息更新之前的退货与进货状态为已结算
        AjaxResult ajaxResult = settleClient.updateStatus(settlementVos);
        if (ajaxResult == null || ajaxResult.isError()) throw new ServiceException("提交结算失败");
        return AjaxResult.success("提交结算成功");
    }


}

