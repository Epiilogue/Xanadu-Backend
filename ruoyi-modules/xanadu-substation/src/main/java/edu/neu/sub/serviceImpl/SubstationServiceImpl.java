package edu.neu.sub.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.ReceiptStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.DailyReport;
import edu.neu.sub.entity.Receipt;
import edu.neu.sub.entity.Substation;
import edu.neu.sub.mapper.SubstationMapper;
import edu.neu.sub.service.DailyReportService;
import edu.neu.sub.service.ReceiptService;
import edu.neu.sub.service.SubstationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 分站 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Service
public class SubstationServiceImpl extends ServiceImpl<SubstationMapper, Substation> implements SubstationService {

    @Autowired
    SubstationMapper substationMapper;

    @Autowired
    DailyReportService dailyReportService;

    @Autowired
    ReceiptService receiptService;

    @Override
    public List<Long> getCourierBySubstationId(Long subId) {
        return substationMapper.getCourierBySubstationId(subId);
    }

    @Override
    public Long getSubstationIdByCourierId(Long courierId) {
        return substationMapper.getSubstationIdByCourierId(courierId);
    }

    @Override
    public List<Long> getAllSubstationManager() {
        return substationMapper.getAllSubstationManager();
    }

    @Override
    public Substation getByManagerId(Long userId) {
        return substationMapper.getByManagerId(userId);
    }

    @Override
    public void removeMasters(Long id) {
        substationMapper.removeMasters(id);
    }

    @Override
    public List<Long> getAllCourier() {
        return substationMapper.getAllCourier();
    }

    @Override
    public void addCourier(Long substationId, List<Long> courierIds) {
        courierIds.forEach(courierId -> substationMapper.addCourier(substationId, courierId));
    }

    @Override
    public List<Long> getCourierList(Long substationId) {
        return substationMapper.getCourierList(substationId);
    }

    @Override
    public int deleteCourier(Long substationId, Long courierId) {
        return substationMapper.deleteCourier(substationId, courierId);
    }

    @Override
    public Integer addMasters(Long id, List<Long> adminIds) {
        int count = 0;
        for (Long adminId : adminIds) {
            count += substationMapper.addMaster(id, adminId);
        }
        return count;
    }

    @Override
    public List<Long> getSubstationMatsers(Long substationId) {
        return substationMapper.getSubstationMasters(substationId);
    }

    @Override
    public int deleteManager(Long substationId, Long userId) {
        return substationMapper.deleteManager(substationId, userId);
    }

    @Override
    public void sayHello() {
        System.out.println("Hello");
    }


    @Override
    public Boolean generateSubstationStatistics() {
        //先检查当日是否已经生成过了
        Date now = new Date();
        Date start = DateUtil.beginOfDay(now);
        QueryWrapper<DailyReport> dailyReportQueryWrapper = new QueryWrapper<DailyReport>().between("statistic_time", start, now);
        List<DailyReport> dailyReportList = dailyReportService.list(dailyReportQueryWrapper);
        if (dailyReportList.size() > 0) {
            return false;
        }
        //拿到所有的子站
        List<Substation> substationList = this.list();
        //生成实例列表
        Map<Long, DailyReport> dailyReportMap = new HashMap<>();
        //遍历子站
        for (Substation substation : substationList) {
            DailyReport dailyReport = new DailyReport(substation.getId(), substation.getName(), new Date(), false);
            //统计快递员数量
            int courierNum = this.getCourierBySubstationId(substation.getId()).size();
            dailyReport.setCourierNum(courierNum);
            dailyReportMap.put(substation.getId(), dailyReport);
        }
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
            d.setIsSettled(false);
            //计算待缴费钱为实际收款额-退款额-配送费
            d.setToPay(d.getReceive() - d.getRefund() - d.getDeliveryFee());
            //实际满意度为满意度总和/任务总数
            d.setFeedback(d.getFeedback() / (d.getFinishTaskNum() + d.getFailTaskNum() + d.getPartFinishTaskNum()));
            d.setStatisticTime(now);
        });
        dailyReportService.saveBatch(dailyReportMap.values());
        return true;
    }

    private Double formula(Double money, Integer satisfaction) {
        return money * 0.03 * (1.0 + (satisfaction - 0.5));
    }

}
