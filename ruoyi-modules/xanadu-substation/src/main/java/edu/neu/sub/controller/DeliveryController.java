package edu.neu.sub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.Delivery;
import edu.neu.sub.entity.Receipt;
import edu.neu.sub.entity.Task;
import edu.neu.sub.service.ReceiptService;
import edu.neu.sub.service.SubstationService;
import edu.neu.sub.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 投递费结算 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@RestController
@RequestMapping("/sub/delivery")
@Transactional
public class DeliveryController {
    @Autowired
    ReceiptService receiptService;

    @Autowired
    TaskService taskService;

    @Autowired
    SubstationService substationService;

    /**
     * 待完成任务数，已完成总任务数，今日完成数，今日待结配送费，计算公式,平均满意度,小程序使用
     */
    @ApiOperation(value = "根据快递员ID，结合当前日期，查询信息")
    @GetMapping("/getTodayDeliveryInfo")
    public AjaxResult getTodayDeliveryInfo(@RequestParam("courierId") Long courierId) {
        //先根据快递员ID拿到分站ID
        Long substationId = substationService.getSubstationIdByCourierId(courierId);
        //检查是否属于某一分站
        if (substationId == null) return AjaxResult.error("该快递员尚未分配分站");
        Delivery delivery = new Delivery();
        //待完成任务数：当前已分配或者已领货的任务数量
        QueryWrapper<Task> queryWrapper = new QueryWrapper<Task>().eq("courier_id", courierId).
                and(q -> q.eq("task_status", TaskStatus.ASSIGNED).or().eq("task_status", TaskStatus.RECEIVED));
        long count = taskService.count(queryWrapper);
        delivery.setUnfinishedTask((int) count);
        //已完成总任务数：回执表中，已完成的任务数量
        QueryWrapper<Receipt> receiptQueryWrapper = new QueryWrapper<Receipt>().eq("courier_id", courierId);
        long receiptCount = receiptService.count(receiptQueryWrapper);
        delivery.setFinishedTask((int) receiptCount);
        //今日已完成
        //1.获取今日日期
        Date now = new Date();
        //2.组装时间范围，从当日0点到当前时间
        @SuppressWarnings("deprecation")
        Date startTime = new Date(now.getYear(), now.getMonth(), now.getDate(), 0, 0, 0);
        delivery.setCourierId(courierId);
        delivery.setSubstationId(substationId);
        QueryWrapper<Receipt> todayReceiptQueryWrapper = new QueryWrapper<Receipt>().eq("courier_id", courierId).between("create_time", startTime, now);
        List<Receipt> list = receiptService.list(todayReceiptQueryWrapper);
        delivery.setTodayFinishedTask(list.size());

        //今日待结算：送货或者送货收款的每一单实收款*(1.0+(客户满意度-0.5))
        double todayUnfinishedMoney = 0.0;
        for (Receipt receipt : list) {
            if (receipt.getTaskType().equals(TaskType.PAYMENT_DELIVERY) || receipt.getTaskType().equals(TaskType.DELIVERY)) {
                todayUnfinishedMoney += formula(receipt.getInputMoney(), receipt.getFeedback());
            }
        }
        delivery.setTodayDeliveryFee(todayUnfinishedMoney);
        //平均满意度：回执表中，已完成的任务的满意度的平均值
        QueryWrapper<Receipt> feedbackQueryWrapper = new QueryWrapper<Receipt>().eq("courier_id", courierId);
        List<Receipt> feedbackList = receiptService.list(feedbackQueryWrapper);
        Double feedbackSum = 0.0;
        for (Receipt receipt : feedbackList)
            feedbackSum += receipt.getFeedback();

        delivery.setAverageSatisfaction(feedbackSum / feedbackList.size());
        return AjaxResult.success(delivery);
    }

    /**
     * 配送费为送货或者送货收款的每一单实收款*0.03*(1.0+(客户满意度-0.5))
     */
    private Double formula(Double money, Integer satisfaction) {
        return money * 0.03 * (1.0 + (satisfaction - 0.5));
    }


}

