package edu.neu.sub.controller;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import edu.neu.base.constant.cc.ReceiptStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.base.constant.cc.UserRoles;
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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    RemoteUserService remoteUserService;


    @GetMapping("/list")
    @ApiOperation(value = "分站列表,只查找自己所管理的")
    public AjaxResult list() {
        Long userId = SecurityUtils.getUserId();//获取当前用户的id
        List<Substation> substationList = substationService.listByManagerId(userId);
        if (substationList == null || substationList.size() == 0) {
            return AjaxResult.success("当前用户没有管理任何分站");
        }
        return AjaxResult.success(substationList);
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "分站列表,查找所有的,管理员专用")
    public AjaxResult listAll() {
        List<Substation> substationList = substationService.list();
        if (substationList == null || substationList.size() == 0) {
            return AjaxResult.success("当前用户没有管理任何分站");
        }
        return AjaxResult.success(substationList);
    }


    @ApiOperation(value = "获取所有的分站管理角色对应的用户列表，过滤掉已经分配过的，但是不过滤管理员")
    @GetMapping("/getSubstationUserList")
    public AjaxResult getSubstationUserList() {
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.SUBSTATION_MANAGER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //过滤掉已经被分配的管理人，但如果是管理员，就不过滤
        //拿到所有的已经分配过id
        List<Long> allSubstationManagerIds = substationService.getAllSubstationManager();
        //过滤掉userlist中已经出现过并且不是管理员身份的用户列表
        userList = userList.stream().filter(user -> {
                    //如果是管理员，就不过滤
                    for (SysRole role : user.getRoles()) {
                        if (Objects.equals(role.getRoleName(), UserRoles.ADMIN)) return true;
                    }
                    return !allSubstationManagerIds.contains(user.getUserId());
                }
        ).collect(Collectors.toList());
        return AjaxResult.success(userList);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加分站,")
    public AjaxResult add(@RequestBody Substation substation) {
        //添加分站，需要注意的是分站仓库是一对一
        //分站和管理人是多对多
        //分站与快递员是一对多
        //如果有，就不能添加
        QueryWrapper<Substation> subwareEq = new QueryWrapper<Substation>().eq("subware_id", substation.getSubwareId());
        if (substationService.getOne(subwareEq) != null) return AjaxResult.error("该仓库已经被其他分站使用");
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        if (substationService.getOne(addressEq) != null) return AjaxResult.error("同地址下已有分站");
        substationService.save(substation);
        //检查是否存在相同地址
        return AjaxResult.success("添加成功");
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新分站信息")
    public AjaxResult update(@RequestBody Substation substation) {
        //更新分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加
        //检查是否存在相同地址
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        Substation one = substationService.getOne(addressEq);
        if (one != null && !Objects.equals(one.getId(), substation.getId()))
            return AjaxResult.error("同地址下已有分站");
        //不允许更新分库以及管理员
        Substation byId = substationService.getById(substation.getId());
        if (!Objects.equals(byId.getSubwareId(), substation.getSubwareId())) return AjaxResult.error("不允许更新分库");
        substationService.updateById(substation);
        return AjaxResult.success("更新成功");
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除分站,连带删除关系")
    public AjaxResult delete(@PathVariable("id") Long id) {
        //删除分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加
        Substation byId = substationService.getById(id);
        if (byId == null) return AjaxResult.error("分站不存在");
        //检查是否存在相同地址,需要进行远程调用，查询订单表，是否存在分站ID，如果存在则无法删除、
        Boolean orderCountBySubstationId = orderClient.getOrderCountBySubstationId(id);
        if (orderCountBySubstationId) return AjaxResult.error("该分站存在订单，无法删除");
        substationService.removeById(id);
        substationService.removeMasters(id);
        return AjaxResult.success("删除成功");
    }


    /**
     * 分站快递员管理，增加删除分站快递员
     */

    @ApiOperation(value = "获取快递员身份用户列表，过滤掉已经是分站快递员的用户，但是不过滤管理员")
    @GetMapping("/getCourierUserList")
    public AjaxResult getCourierUserList() {
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.COURIER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //过滤掉已经被分配的管理人，但如果是管理员，就不过滤
        //拿到所有的已经分配过id
        List<Long> allCourierIds = substationService.getAllCourier();
        //过滤掉userlist中已经出现过并且不是管理员身份的用户列表
        userList = userList.stream().filter(user -> {
                    //如果是管理员，就不过滤
                    for (SysRole role : user.getRoles()) {
                        if (Objects.equals(role.getRoleName(), UserRoles.ADMIN)) return true;
                    }
                    return !allCourierIds.contains(user.getUserId());
                }
        ).collect(Collectors.toList());
        return AjaxResult.success(userList);
    }

    @PostMapping("/addCourier/{substationId}}")
    @ApiOperation(value = "批量添加分站快递员")
    public AjaxResult addCourier(@PathVariable("substationId") Long substationId, @RequestBody List<Long> courierIds) {
        //添加分站快递员，需要注意的是分站快递员是一对多
        //如果有，就不能添加
        substationService.addCourier(substationId, courierIds);
        return AjaxResult.success("添加成功");
    }

    @ApiOperation(value = "获取当前分站快递员列表")
    @GetMapping("/getCourierList/{substationId}")
    public AjaxResult getCourierList(@PathVariable("substationId") Long substationId) {

        //如果有，就不能添加
        List<Long> courierIdList = substationService.getCourierList(substationId);

        //TODO: 根据id列表查询用户列表
        return null;

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

