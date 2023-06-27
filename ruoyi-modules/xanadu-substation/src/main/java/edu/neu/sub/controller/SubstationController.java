package edu.neu.sub.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.ReceiptStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.DailyReport;
import edu.neu.sub.entity.Receipt;
import edu.neu.sub.entity.Substation;
import edu.neu.sub.entity.Task;
import edu.neu.sub.feign.OrderClient;
import edu.neu.sub.service.DailyReportService;
import edu.neu.sub.service.ReceiptService;
import edu.neu.sub.service.SubstationService;
import edu.neu.sub.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 分站 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@RestController
@RequestMapping("/sub/substation")
public class SubstationController {
    //分站的增删改查方法，需要制定管理人以及附属仓库，都是一对一关系

    @Autowired
    SubstationService substationService;

    @Autowired
    TaskService taskService;


    @Autowired
    OrderClient orderClient;

    @Autowired
    DailyReportService dailyReportService;

    @Autowired
    ReceiptService receiptService;


    @GetMapping("/list")
    @ApiOperation(value = "分站列表")
    public AjaxResult list() {
        return AjaxResult.success(substationService.list());
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加分站")
    public AjaxResult add(@RequestBody Substation substation) {
        //添加分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加

        QueryWrapper<Substation> userEq = new QueryWrapper<Substation>().eq("user_id", substation.getUserId());
        if (substationService.getOne(userEq) != null) return AjaxResult.error("该管理人已经管理了其他分站");
        QueryWrapper<Substation> subwareEq = new QueryWrapper<Substation>().eq("subware_id", substation.getSubwareId());
        if (substationService.getOne(subwareEq) != null) return AjaxResult.error("该仓库已经被其他分站使用");
        //检查是否存在相同地址
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        if (substationService.getOne(addressEq) != null) return AjaxResult.error("同地址下已有分站");
        substationService.save(substation);
        return AjaxResult.success("添加成功");
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新分站")
    public AjaxResult update(@RequestBody Substation substation) {
        //更新分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加
        //检查是否存在相同地址
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        Substation substation1 = substationService.getOne(addressEq);
        if(substation1.getId()==null)return AjaxResult.error("该分站不存在");
        if (!Objects.equals(substationService.getOne(addressEq).getId(), substation.getId())) return AjaxResult.error("同地址下已有分站");
        //不允许更新子站
        Substation byId = substationService.getById(substation.getId());
        if (!Objects.equals(byId.getSubwareId(), substation.getSubwareId())) return AjaxResult.error("不允许更新分库ID");
        substationService.updateById(substation);
        return AjaxResult.success("更新成功");
    }


    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除分站")
    public AjaxResult delete(@PathVariable("id") Long id) {
        //删除分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加
        Substation byId = substationService.getById(id);
        if (byId == null) return AjaxResult.error("分站不存在");
        //检查是否存在相同地址,需要进行远程调用，查询订单表，是否存在分站ID，如果存在则无法删除、
        Boolean orderCountBySubstationId = orderClient.getOrderCountBySubstationId(id);
        if (orderCountBySubstationId) return AjaxResult.error("该分站存在订单，无法删除");
        substationService.removeById(id);
        return AjaxResult.success("删除成功");
    }


    @GetMapping("/feign/getSubwareId/{id}")
    @ApiOperation(value = "获取分站的仓库ID")
    public AjaxResult getSubwareId(@PathVariable("id") Long id) {
        Substation substation = substationService.getById(id);
        if (substation == null) return AjaxResult.error("分站不存在");
        return AjaxResult.success(substation.getSubwareId());
    }

    @GetMapping("/feign/getSubstationId/{subwareId}")
    @ApiOperation(value = "获取分站的ID")
    public AjaxResult getSubstationId(@PathVariable("subwareId") Long subwareId) {
        QueryWrapper<Substation> eq = new QueryWrapper<Substation>().eq("subware_id", subwareId);
        List<Substation> list = substationService.list(eq);
        if (list == null || list.size() == 0) return AjaxResult.error("分库ID对应的分站不存在");
        if (list.size() > 1) return AjaxResult.error("分库ID对应的分站不唯一");
        Substation substation = list.get(0);
        return AjaxResult.success(substation.getId());
    }

    /**
     * 定时任务，每晚远程调用。统计有当日每个分站的快递员数量，任务类型数量，完成度数量，送货数量，收款额，退回数量，
     * 退款额，实收款，配送费，待缴款，是否缴款，能够查看历史是否结算等信息
     * 直接获取一个列表，这个需要保存到数据库，允许历史查看
     */

    @PostMapping("/feign/generateSubstationStatistics")
    @ApiOperation(value = "生成当日分站统计信息，每日定时任务")
    public AjaxResult generateSubstationStatistics() {
        //拿到所有的子站
        List<Substation> substationList = substationService.list();
        //生成实例列表
        Map<Long, DailyReport> dailyReportMap = new HashMap<>();
        //遍历子站
        for (Substation substation : substationList) {
            DailyReport dailyReport = new DailyReport(substation.getId(), substation.getName(), new Date(), false);
            //统计快递员数量
            int courierNum = substationService.getCourierBySubstationId(substation.getId()).size();
            dailyReport.setCourierNum(courierNum);
            dailyReportMap.put(substation.getId(), dailyReport);
        }
        //拿到当日的开始时间，以及当前时间作为时间范围，获取当日所有的已完成，失败，部分完成状态的任务单
        Date now = new Date();
        Date start = DateUtil.beginOfDay(now);
        QueryWrapper<Receipt> receiptQueryWrapper = new QueryWrapper<Receipt>().between("create_time", start, now);
        //获取当日所有的的回执
        List<Receipt> receiptList = receiptService.list(receiptQueryWrapper);
        //遍历回执，更新对应的子站统计信息
        for (Receipt receipt : receiptList) {
            //获取子站统计信息
            DailyReport dailyReport = dailyReportMap.get(receipt.getSubId());
            //根据当前的receipt的状态，更新对应的子站统计信息
            switch (receipt.getTaskType()) {
                case TaskType.DELIVERY:
                    dailyReport.setDeliveryTaskNum(dailyReport.getDeliveryTaskNum() + 1);
                    dailyReport.setSignNum(dailyReport.getSignNum() + receipt.getPlanNum());
                    dailyReport.setRefund(dailyReport.getRefund() + receipt.getOutputMoney());
                    //计算配送费
                    dailyReport.setDeliveryFee(dailyReport.getDeliveryFee() + formula(receipt.getInputMoney(), receipt.getFeedback()));
                    break;
                case TaskType.RETURN:
                    dailyReport.setReturnTaskNum(dailyReport.getReturnTaskNum() + 1);
                    dailyReport.setReturnNum(dailyReport.getReturnNum() + receipt.getRefundNum());
                    dailyReport.setRefund(dailyReport.getRefund() + receipt.getOutputMoney());
                    break;
                case TaskType.PAYMENT_DELIVERY:
                    dailyReport.setDeliveryReceiveTaskNum(dailyReport.getDeliveryReceiveTaskNum() + 1);
                    dailyReport.setSignNum(dailyReport.getSignNum() + receipt.getPlanNum());
                    dailyReport.setReturnNum(dailyReport.getReturnNum() + receipt.getRefundNum());
                    dailyReport.setRefund(dailyReport.getRefund() + receipt.getOutputMoney());
                    dailyReport.setReceive(dailyReport.getReceive() + receipt.getInputMoney());
                    //计算配送费
                    dailyReport.setDeliveryFee(dailyReport.getDeliveryFee() + formula(receipt.getInputMoney(), receipt.getFeedback()));
                    break;
                case TaskType.PAYMENT:
                    dailyReport.setReceiveTaskNum(dailyReport.getReceiveTaskNum() + 1);
                    dailyReport.setReceive(dailyReport.getReceive() + receipt.getInputMoney());
                    break;
                case TaskType.EXCHANGE:
                    dailyReport.setExchangeTaskNum(dailyReport.getExchangeTaskNum() + 1);
                    dailyReport.setSignNum(dailyReport.getSignNum() + receipt.getSignNum());
                    dailyReport.setReturnNum(dailyReport.getReturnNum() + receipt.getRefundNum());
                    break;
            }
            switch (receipt.getState()) {
                case ReceiptStatus.COMPLETED:
                    dailyReport.setFinishTaskNum(dailyReport.getFinishTaskNum() + 1);
                    break;
                case ReceiptStatus.FAILED:
                    dailyReport.setFailTaskNum(dailyReport.getFailTaskNum() + 1);
                    break;
                case ReceiptStatus.PARTIAL_COMPLETED:
                    dailyReport.setPartFinishTaskNum(dailyReport.getPartFinishTaskNum() + 1);
                    break;
            }
            //满意度累加，最后再统一除任务数计算平均满意度
            dailyReport.setFeedback(dailyReport.getFeedback() + receipt.getFeedback());
        }
        dailyReportMap.values().forEach(d -> {
            //计算待缴费钱为实际收款额-退款额-配送费
            d.setToPay(d.getReceive() - d.getRefund() - d.getDeliveryFee());
            //实际满意度为满意度总和/任务总数
            d.setFeedback(d.getFeedback() / (d.getFinishTaskNum() + d.getFailTaskNum() + d.getPartFinishTaskNum()));
        });
        return AjaxResult.success(new ArrayList<>(dailyReportMap.values()));
    }


    private Double formula(Double money, Integer satisfaction) {
        return money * 0.03 * (1.0 + (satisfaction - 0.5));
    }

}

