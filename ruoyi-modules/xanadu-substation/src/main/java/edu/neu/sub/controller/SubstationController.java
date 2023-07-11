package edu.neu.sub.controller;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import edu.neu.base.constant.cc.UserRoles;
import edu.neu.sub.entity.DailyReport;
import edu.neu.sub.entity.Substation;
import edu.neu.sub.feign.OrderClient;
import edu.neu.sub.feign.SubwareClient;
import edu.neu.sub.service.DailyReportService;
import edu.neu.sub.service.ReceiptService;
import edu.neu.sub.service.SubstationService;
import edu.neu.sub.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
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

    @Autowired
    RemoteUserService remoteUserService;

    @Autowired
    SubwareClient subwareClient;

    @GetMapping("/infoByUser/{userId}")
    @ApiOperation(value = "根据用户id获取分站信息")
    public AjaxResult infoByUser(@PathVariable("userId") Long userId) {
        Substation substation = substationService.getByManagerId(userId);
        if (substation == null) {
            throw new ServiceException("该用户尚未管理分站");
        }
        return AjaxResult.success(substation);
    }

    @GetMapping("/infoById/{subId}")
    @ApiOperation(value = "根据分站id获取分站信息")
    public AjaxResult infoById(@PathVariable("subId") Long subId) {
        Substation substation = substationService.getById(subId);
        if (substation == null) {
            return AjaxResult.error("分站不存在");
        }
        return AjaxResult.success(substation);
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "分站列表,查找所有的,管理员专用，如果登录用户是管理员的话直接请求该接口")
    public AjaxResult listAll() {
        List<Substation> substationList = substationService.list();
        if (substationList == null || substationList.size() == 0) {
            return AjaxResult.success("当前用户没有管理任何分站");
        }
        return AjaxResult.success(substationList);
    }

    @GetMapping("/listAllStations")
    @ApiOperation(value = "获取所有分站以及中心仓库信息")
    public AjaxResult listAllStations() {
        List<Substation> substationList = substationService.list();
        Object center = subwareClient.getCenterInfo();
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("sub", substationList);
        ajaxResult.put("center", center);
        return ajaxResult;
    }


    @ApiOperation(value = "获取所有的分站管理角色对应的用户列表，过滤掉已经分配过的")
    @GetMapping("/getSubstationUserList")
    public AjaxResult getSubstationUserList() {
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.SUBSTATION_MANAGER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //过滤掉已经被分配的管理人，但如果是管理员，就不过滤
        //拿到所有的已经分配过id
        List<Long> allSubstationManagerIds = substationService.getAllSubstationManager();
        //过滤掉userlist中已经出现过并且不是管理员身份的用户列表
        userList = userList.stream().filter(user -> !allSubstationManagerIds.contains(user.getUserId())
        ).collect(Collectors.toList());
        return AjaxResult.success(userList);
    }

    @ApiOperation(value = "获取当前分站的所有管理员信息")
    @GetMapping("/getSubstationManager/{substationId}")
    public AjaxResult getSubstationManager(@PathVariable("substationId") Long substationId) {
        Substation substation = substationService.getById(substationId);
        if (substation == null) throw new ServiceException("分站不存在");
        List<Long> ids = substationService.getSubstationMatsers(substationId);
        if (ids == null || ids.size() == 0) return AjaxResult.success("该分站没有管理员");
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.SUBSTATION_MANAGER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //只留下当前分站的管理员
        userList = userList.stream().filter(user -> ids.contains(user.getUserId())).collect(Collectors.toList());
        //返回
        return AjaxResult.success(userList);
    }


    @PostMapping("/add")
    @ApiOperation(value = "添加分站")
    public AjaxResult add(@RequestBody Substation substation) {
        //添加分站，需要注意的是分站仓库是一对一
        //分站和管理人是多对多
        //分站与快递员是一对多
        //如果有，就不能添加
        QueryWrapper<Substation> subwareEq = new QueryWrapper<Substation>().eq("subware_id", substation.getSubwareId());
        if (substationService.getOne(subwareEq) != null) throw new ServiceException("该仓库已经被其他分站使用");
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        if (substationService.getOne(addressEq) != null) throw new ServiceException("同地址下已有分站");
        substationService.save(substation);
        //需要添加管理员与分站的关系，我们默认此时提交的id里不会存在有已经管理别的分站的ID
        if(substation.getAdminIds()!=null && substation.getAdminIds().size()!=0){
            Integer result = substationService.addMasters(substation.getId(), substation.getAdminIds());
            if (result == 0 || result != substation.getAdminIds().size()) throw new ServiceException("添加库管员失败");
        }
        return AjaxResult.success("添加成功");
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新分站信息，在这里可以更新分站的管理人和仓库，我们需要删除掉原先所有的关系，然后重新添加新的关系")
    public AjaxResult update(@RequestBody Substation substation) {
        //更新分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加
        //检查是否存在相同地址
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        Substation one = substationService.getOne(addressEq);
        if (one != null && !Objects.equals(one.getId(), substation.getId()))
            throw new ServiceException("同地址下已有分站");
        //不允许更新分库
        Substation byId = substationService.getById(substation.getId());
        if (!Objects.equals(byId.getSubwareId(), substation.getSubwareId()))
            throw new ServiceException("不允许更新分库");
        substationService.updateById(substation);
        substationService.removeMasters(substation.getId());
        if(substation.getAdminIds()!=null && substation.getAdminIds().size()!=0){
            substationService.addMasters(substation.getId(), substation.getAdminIds());
        }
        return AjaxResult.success("更新成功");
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除分站,连带删除关系")
    public AjaxResult delete(@PathVariable("id") Long id) {
        //删除分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加
        Substation byId = substationService.getById(id);
        if (byId == null) throw new ServiceException("分站不存在");
        //检查是否存在相同地址,需要进行远程调用，查询订单表，是否存在分站ID，如果存在则无法删除、
        Boolean orderCountBySubstationId = orderClient.getOrderCountBySubstationId(id);
        if (orderCountBySubstationId) throw new ServiceException("该分站存在订单，无法删除");
        substationService.removeById(id);
        substationService.removeMasters(id);
        return AjaxResult.success("删除成功");
    }

    @GetMapping("/feign/getSubstation/{id}")
    @ApiOperation(value = "获取分站的信息")
    public AjaxResult getSubstation(@PathVariable("id") Long id) {
        Substation substation = substationService.getById(id);
        if (substation == null) throw new ServiceException("分站不存在");
        return AjaxResult.success(substation);
    }


    /**
     * 分站快递员管理，增加删除分站快递员
     */

    @ApiOperation(value = "获取可添加的快递员身份用户列表，过滤掉已经是分站快递员的用户，但是不过滤管理员")
    @GetMapping("/getCourierUserList")
    public AjaxResult getCourierUserList() {
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.COURIER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //过滤掉已经被分配的管理人，但如果是管理员，就不过滤
        //拿到所有的已经分配过id
        List<Long> allCourierIds = substationService.getAllCourier();
        //过滤掉userlist中已经出现过并且不是管理员身份的用户列表
        userList = userList.stream().filter(user -> !allCourierIds.contains(user.getUserId())
        ).collect(Collectors.toList());
        return AjaxResult.success(userList);
    }

    @PostMapping("/addCourier/{substationId}")
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
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.COURIER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //只留下id在courierIdList中的用户
        userList = userList.stream().filter(user -> courierIdList.contains(user.getUserId())).collect(Collectors.toList());
        return AjaxResult.success(userList);
    }

    @ApiOperation(value = "删除分站快递员")
    @PostMapping("/deleteCourier/{substationId}/{courierId}")
    public AjaxResult deleteCourier(@PathVariable("substationId") Long substationId, @PathVariable("courierId") Long courierId) {
        //如果有，就不能添加
        int result = substationService.deleteCourier(substationId, courierId);
        if (result == 0) throw new ServiceException("删除失败");
        return AjaxResult.success("删除成功");
    }

    //todo 传入id状态置为true
    @ApiOperation(value = "修改报表状态为已结算")
    @GetMapping("/updateDailyReportStatus/{dailyReportId}")
    public AjaxResult updateDailyReportStatus(@PathVariable("dailyReportId") Long dailyReportId) {
        DailyReport dailyReport = dailyReportService.getById(dailyReportId);
        if (dailyReport == null) {
            throw new ServiceException("该报表不存在");
        }
        dailyReport.setIsSettled(true);
        dailyReportService.updateById(dailyReport);
        return AjaxResult.success("修改成功");
    }

    @ApiOperation(value = "根据日期查询报表")
    @GetMapping("/dailyReportsByDate")
    public AjaxResult getData(@RequestParam("date") String dateStr) {
        Date date = DateUtil.parse(dateStr, "yyyy-MM-dd HH:mm:ss");
        // 在这里处理接收到的Date类型参数
        QueryWrapper<DailyReport> dailyReportQueryWrapper = new QueryWrapper<DailyReport>().between("statistic_time", DateUtil.beginOfDay(date), DateUtil.endOfDay(date));
        List<DailyReport> dailyReports = dailyReportService.list(dailyReportQueryWrapper);
        if (dailyReports == null) {
            throw new ServiceException("暂无数据");
        }
        // 返回你的响应
        return AjaxResult.success(dailyReports);
    }

    @GetMapping("/feign/getSubwareId/{id}")
    @ApiOperation(value = "获取分站的仓库ID")
    public AjaxResult getSubwareId(@PathVariable("id") Long id) {
        Substation substation = substationService.getById(id);
        if (substation == null) throw new ServiceException("分站不存在");
        return AjaxResult.success(substation.getSubwareId());
    }

    @GetMapping("/feign/getSubstationId/{subwareId}")
    @ApiOperation(value = "获取分站的ID")
    public AjaxResult getSubstationId(@PathVariable("subwareId") Long subwareId) {
        QueryWrapper<Substation> eq = new QueryWrapper<Substation>().eq("subware_id", subwareId);
        List<Substation> list = substationService.list(eq);
        if (list == null || list.size() == 0) throw new ServiceException("分库ID对应的分站不存在");
        if (list.size() > 1) throw new ServiceException("分库ID对应的分站不唯一");
        Substation substation = list.get(0);
        return AjaxResult.success(substation.getId());
    }

    /**
     * 定时任务，每晚远程调用。统计有当日每个分站的快递员数量，任务类型数量，完成度数量，送货数量，收款额，退回数量，
     * 退款额，实收款，配送费，待缴款，是否缴款，能够查看历史是否结算等信息
     * 直接获取一个列表，这个需要保存到数据库，允许历史查看
     */

    @GetMapping("/generateSubstationStatistics")
    @ApiOperation(value = "手动生成当日分站统计信息，每日定时任务,把主体逻辑移动到service中，实现幂等性")
    public AjaxResult generateSubstationStatistics() {
        substationService.generateSubstationStatistics();
        Date date = new Date();
        QueryWrapper<DailyReport> dailyReportQueryWrapper = new QueryWrapper<DailyReport>().between("statistic_time", DateUtil.beginOfDay(date), DateUtil.endOfDay(date));
        List<DailyReport> dailyReports = dailyReportService.list(dailyReportQueryWrapper);
        return AjaxResult.success(dailyReports);
    }


    private Double formula(Double money, Integer satisfaction) {
        return money * 0.03 * (1.0 + (satisfaction - 0.5));
    }

}

