package edu.neu.sub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.Receipt;
import edu.neu.sub.entity.ReceiptProduct;
import edu.neu.sub.entity.Task;
import edu.neu.sub.feign.OrderClient;
import edu.neu.sub.service.ReceiptService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 提供业务报表分析的所有数据信息
 */
@RestController
@Transactional(rollbackFor = Exception.class)
public class BusinessController {

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ReceiptService receiptService;

    /**
     * 列表显示客服在一定时间段内订购前五名的商品情况
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param number    前几名
     */
    @GetMapping("/listTopProduct/{number}")
    public AjaxResult listTopProducts(@RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam("endTime")@DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam("number") Integer number) {
        AjaxResult ajaxResult = orderClient.listTopProducts(startTime, endTime, number);
        if (ajaxResult == null) return AjaxResult.error("获取失败");
        return ajaxResult;
    }

    /**
     * 分站配送情况分析
     * 是按条件查询出一年或一个月分站完成任务的情况，送货付款任务数、送货商品数量、送货付款金额
     * 前端需要采集到用户的输入 一年或者一个月然后转化为开始结束时间传给后端，也允许自主选择时间段
     */
    @GetMapping("/getSubstationDeliveryAnalysis/{substationId}}")
    public AjaxResult getSubstationDeliveryAnalysis(@PathVariable("substationId") Long substationId,
                                                    @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                    @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        //检查时间是否合法
        if (startTime == null || endTime == null) return AjaxResult.error("时间不能为空");
        if (startTime.after(endTime)) return AjaxResult.error("开始时间不能大于结束时间");
        if (substationId == null) return AjaxResult.error("分站ID不能为空");
        @Data
        class Record {
            Integer payTask;
            Integer deliveryTask;
            Integer payAndDeliveryTask;
            Integer changeTask;
            Integer refundTask;
            Integer deliveryNumber;//送货数量
            Double deliveryAmount;//收款金额
            Integer refundNumber;//退货数量
            Double refundAmount;//退款金额
        }
        Record record = new Record();
        //1.根据时间段以及分站ID拿到所有的收据信息
        QueryWrapper<Receipt> queryWrapper = new QueryWrapper<Receipt>().eq("sub_id", substationId).between("create_time", startTime, endTime);
        List<Receipt> list = receiptService.list(queryWrapper);
        if (list == null || list.size() == 0) return AjaxResult.error("该分站在该时间段内没有完成任务");
        //统计每个任务量出现的次数
        record.payTask = list.stream().filter(receipt -> receipt.getTaskType().equals(TaskType.PAYMENT)).toArray().length;
        record.deliveryTask = list.stream().filter(receipt -> receipt.getTaskType().equals(TaskType.DELIVERY)).toArray().length;
        record.payAndDeliveryTask = list.stream().filter(receipt -> receipt.getTaskType().equals(TaskType.PAYMENT_DELIVERY)).toArray().length;
        record.changeTask = list.stream().filter(receipt -> receipt.getTaskType().equals(TaskType.EXCHANGE)).toArray().length;
        record.refundTask = list.stream().filter(receipt -> receipt.getTaskType().equals(TaskType.RETURN)).toArray().length;
        record.deliveryNumber = list.stream().filter(r -> r.getTaskType().equals(TaskType.DELIVERY) || r.getTaskType().equals(TaskType.PAYMENT_DELIVERY) || r.getTaskType().equals(TaskType.EXCHANGE)).mapToInt(Receipt::getPlanNum).sum();
        record.deliveryAmount = list.stream().filter(r -> r.getTaskType().equals(TaskType.PAYMENT_DELIVERY) || r.getTaskType().equals(TaskType.PAYMENT)).mapToDouble(Receipt::getPlanReceipt).sum();
        record.refundNumber = list.stream().filter(r -> !r.getTaskType().equals(TaskType.PAYMENT)).mapToInt(Receipt::getRefundNum).sum();
        record.refundAmount = list.stream().filter(r -> !r.getTaskType().equals(TaskType.PAYMENT)).mapToDouble(Receipt::getOutputMoney).sum();
        return AjaxResult.success(record);
    }

    @GetMapping("/getSubstationDeliveryInfo")
    @ApiOperation(value = "数据大屏使用")
    public AjaxResult getSubstationDeliveryInfo() {
        @Data
        class Record {
            Long subId;
            Double deliveryAmount;//收款金额
            Long totalOrders;//总订单数
            Long delivered;//配送订单数
            Long paymentDelivered;//送货付款订单数
        }
        List<Record> records = new ArrayList<>();
        //1.根据时间段以及分站ID拿到所有的收据信息
        List<Receipt> receiptList = receiptService.list();
        if (receiptList == null || receiptList.size() == 0) return AjaxResult.error("不存在记录");
        List<Long> subIds = receiptList.stream()
                .map(Receipt::getSubId)
                .distinct()
                .collect(Collectors.toList());
        //已换货订单
        Map<Long, Long> exchangedCounts = receiptList.stream()
                .filter(receipt -> receipt.getTaskType().equals(TaskType.EXCHANGE))
                .collect(Collectors.groupingBy(Receipt::getSubId, Collectors.counting()));
        // 配送订单数
        Map<Long, Long> deliveryCounts = receiptList.stream()
                .filter(receipt -> receipt.getTaskType().equals(TaskType.DELIVERY))
                .collect(Collectors.groupingBy(Receipt::getSubId, Collectors.counting()));
        //送货收款订单
        Map<Long, Long> paymentDeliveryCounts = receiptList.stream()
                .filter(receipt -> receipt.getTaskType().equals(TaskType.PAYMENT_DELIVERY))
                .collect(Collectors.groupingBy(Receipt::getSubId, Collectors.counting()));
        //退货订单数
        Map<Long, Long> returnCounts = receiptList.stream()
                .filter(receipt -> receipt.getTaskType().equals(TaskType.RETURN))
                .collect(Collectors.groupingBy(Receipt::getSubId, Collectors.counting()));
        //收款订单数
        Map<Long, Long> paymentCounts = receiptList.stream()
                .filter(receipt -> receipt.getTaskType().equals(TaskType.PAYMENT_DELIVERY))
                .collect(Collectors.groupingBy(Receipt::getSubId, Collectors.counting()));
        // 分库收款金额
        Map<Long, Double> paymentAmounts = receiptList.stream()
                .filter(receipt -> receipt.getTaskType().equals(TaskType.PAYMENT_DELIVERY) || receipt.getTaskType().equals(TaskType.PAYMENT))
                .collect(Collectors.groupingBy(Receipt::getSubId, Collectors.summingDouble(Receipt::getInputMoney)));
        for (Long subId : subIds) {
            Record record = new Record();
            Long payment = paymentCounts.getOrDefault(subId, 0L);
            Long delivery = deliveryCounts.getOrDefault(subId, 0L);
            Long paymentDelivery = paymentDeliveryCounts.getOrDefault(subId, 0L);
            Long returned = returnCounts.getOrDefault(subId, 0L);
            Long exchange = exchangedCounts.getOrDefault(subId, 0L);
            Long total = paymentDelivery + payment + delivery + returned + exchange;
            record.deliveryAmount = paymentAmounts.getOrDefault(subId, 0.0);
            record.subId = subId;
            record.totalOrders = total;
            record.delivered = delivery;
            record.paymentDelivered = paymentDelivery;
            records.add(record);
        }

        return AjaxResult.success(records);
    }

    /**
     * 客户满意度分析，需要给出平均分，以及每个分段的数量0-10，输入信息分站ID，时间段（不输默认全部）
     */
    @GetMapping("/getCustomerSatisfactionAnalysis")
    public AjaxResult getCustomerSatisfactionAnalysis(@RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                      @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        //检查时间是否合法
        if (startTime == null || endTime == null) return AjaxResult.error("时间不能为空");
        if (startTime.after(endTime)) return AjaxResult.error("开始时间不能大于结束时间");
        QueryWrapper<Receipt> queryWrapper = new QueryWrapper<Receipt>().between("create_time", startTime, endTime);
        List<Receipt> list = receiptService.list(queryWrapper);
        if (list == null || list.size() == 0) return AjaxResult.error("该时间段内没有任务完成");
        int[] arr = new int[11];
        //1.拿到所有的满意度，然后添加到map中
        list.stream().map(Receipt::getFeedback).forEach(satisfaction -> {
            if (satisfaction == null) return;
            arr[satisfaction]++;
        });
        //计算平均数
        OptionalDouble average = list.stream().mapToInt(Receipt::getFeedback).average();
        Double avg = average.isPresent() ? average.getAsDouble() : 0;
        Map<String, Object> map = new HashMap<>();
        map.put("avg", avg);
        map.put("arr", arr);//0-10的数量
        return AjaxResult.success(map);
    }


}
