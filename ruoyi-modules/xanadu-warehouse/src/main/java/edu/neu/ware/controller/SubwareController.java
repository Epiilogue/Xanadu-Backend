package edu.neu.ware.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;

import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUser;
import edu.neu.base.constant.cc.UserRoles;
import edu.neu.ware.entity.SubStorageRecord;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.feign.CCOrderClient;
import edu.neu.ware.service.SubStorageRecordService;
import edu.neu.ware.service.SubwareService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 03:42:21
 */
@RestController
@Transactional(rollbackFor = Exception.class)
@RequestMapping("/ware/subware")
@CacheConfig(cacheNames = "subware")
public class SubwareController {

    //分库的增加,前端需要选择好城市等信息，还需要选择库管员
    //修改，允许修改地址等，
    //删除，即仓库无货物并且没有未完成的订单或者任务单才能够删除
    @Autowired
    private SubwareService subwareService;

    @Autowired
    private CCOrderClient ccOrderClient;

    @Autowired
    private SubStorageRecordService subStorageRecordService;

    @Autowired
    private RemoteUserService remoteUserService;

    @PostMapping("/add")
    @ApiOperation(value = "添加分库", notes = "添加分库")
    public AjaxResult addSubware(@RequestBody Subware subware) {

        String valitedResult = subwareService.validateSubware(subware);
        if (valitedResult != null) throw new ServiceException(valitedResult);

        boolean res = subwareService.save(subware);
        if (!res) throw new ServiceException("创建分库失败");
        Integer count = subwareService.addMasters(subware.getId(), subware.getManagerIds());
        if (count != subware.getManagerIds().size()) throw new ServiceException("添加分库管理员失败");
        return AjaxResult.success("创建分库成功", subware);
    }

    /**
     * 2.批量添加库管员
     * 3.更新时候重置库管员关系
     * 5.获取自己管理的分库信息
     */

    @ApiOperation(value = "获取所有的分库管理角色对应的用户列表，过滤掉已经分配过的")
    @GetMapping("/getSubwareUserList")
    public AjaxResult getSubwareUserList() {
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.SUB_WAREHOUSE_MANAGER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //过滤掉已经被分配的管理人，但如果是管理员，就不过滤
        //拿到所有的已经分配过id
        List<Long> allSubwareManagerIds = subwareService.getAllSubwareManager();
        userList = userList.stream().filter(user -> !allSubwareManagerIds.contains(user.getUserId())
        ).collect(Collectors.toList());
        return AjaxResult.success(userList);
    }

    @GetMapping("/infoByUser/{userId}")
    @ApiOperation(value = "根据用户id获取分库信息,拿到自己管理的分库")
    public AjaxResult infoByUser(@PathVariable("userId") Long userId) {
        Subware subware = subwareService.getByManagerId(userId);
        if (subware == null) {
            return AjaxResult.success("该用户没有管理分库");
        }
        return AjaxResult.success(subware);
    }

    @ApiOperation(value = "获取当前分库的所有管理员信息")
    @GetMapping("/getSubwareManager/{subwareId}")
    public AjaxResult getSubwareManager(@PathVariable("subwareId") Long subwareId) {
        Subware subware = subwareService.getById(subwareId);
        if (subware == null) throw new ServiceException("分库不存在");
        List<Long> ids = subwareService.getSubwareMatsers(subwareId);
        if (ids == null || ids.size() == 0) return AjaxResult.success("该分库没有管理员");
        AjaxResult ajaxResult = remoteUserService.listByRole(UserRoles.SUB_WAREHOUSE_MANAGER);
        List<SysUser> userList = JSON.parseArray(JSON.toJSONString(ajaxResult.get("data")), SysUser.class);
        //只留下当前分库的管理员
        userList = userList.stream().filter(user -> ids.contains(user.getUserId())).collect(Collectors.toList());
        //返回
        return AjaxResult.success(userList);
    }


    @PostMapping("/update")
    @ApiOperation(value = "修改分库", notes = "修改分库")
    @CacheEvict(key = "#subware.id")
    public AjaxResult updateSubware(@RequestBody Subware subware) {

        String valitedResult = subwareService.validateSubware(subware);
        if (valitedResult != null) throw new ServiceException(valitedResult);
        boolean res = subwareService.updateById(subware);
        if (!res) throw new ServiceException("修改分库失败");

        //删除原有关系，更新新的关系
        subwareService.removeMasters(subware.getId());
        Integer count = subwareService.addMasters(subware.getId(), subware.getManagerIds());
        if (count != subware.getManagerIds().size()) throw new ServiceException("更新分库管理员失败");

        return AjaxResult.success("修改分库成功", subware);
    }

    @GetMapping("/delete/{id}")
    @CacheEvict(key = "#id")
    @ApiOperation(value = "删除分库", notes = "删除分库")
    public AjaxResult deleteSubware(@PathVariable Long id) {
        Subware subware = subwareService.getById(id);
        if (subware == null) throw new ServiceException("分库不存在");

        QueryWrapper<SubStorageRecord> totalNum = new QueryWrapper<SubStorageRecord>().gt("total_num", 0);
        List<SubStorageRecord> list = subStorageRecordService.list(totalNum);
        if (list.size() > 0) throw new ServiceException("分库有货物，不能删除");

        boolean res = subwareService.removeById(id);
        if (!res) throw new ServiceException("删除分库失败");
        subwareService.removeMasters(id);
        return AjaxResult.success("删除分库成功", subware);
    }

    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "获取分库列表", notes = "获取分库列表")
    public AjaxResult getSubwareList(@PathVariable Integer page, @PathVariable Integer size) {
        List<Subware> subwareList = subwareService.page(new Page<>(page, size)).getRecords();
        if (subwareList == null) throw new ServiceException("获取分库列表失败");
        return AjaxResult.success("获取分库列表成功", subwareList);
    }

    @GetMapping("/get/{id}")
    @Cacheable(key = "#id")
    @ApiOperation(value = "获取分库", notes = "获取分库")
    public AjaxResult getSubware(@PathVariable Long id) {
        Subware subware = subwareService.getById(id);
        if (subware == null) throw new ServiceException("获取分库失败");
        return AjaxResult.success("获取分库成功", subware);
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有分库", notes = "获取所有分库")
    public AjaxResult getAllSubware() {
        List<Subware> subwareList = subwareService.list();
        if (subwareList == null) throw new ServiceException("获取分库列表失败");
        return AjaxResult.success("获取分库列表成功", subwareList);
    }

    @GetMapping("/feign/info/{id}")
    @ApiOperation(value = "根据分库ID获取分库信息")
    public AjaxResult info(@PathVariable("id") Long id) {
        Subware subware = subwareService.getById(id);
        if (subware == null) throw new ServiceException("分库不存在");
        return AjaxResult.success(subware);
    }

}
