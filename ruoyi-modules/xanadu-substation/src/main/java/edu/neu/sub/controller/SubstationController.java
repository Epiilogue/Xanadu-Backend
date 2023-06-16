package edu.neu.sub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.sub.entity.Substation;
import edu.neu.sub.feign.OrderClient;
import edu.neu.sub.service.SubstationService;
import edu.neu.sub.service.TaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
        substationService.save(substation);
        //检查是否存在相同地址
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        if (substationService.getOne(addressEq) != null) return AjaxResult.error("同地址下已有分站");
        return AjaxResult.success("添加成功");
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新分站")
    public AjaxResult update(@RequestBody Substation substation) {
        //更新分站，需要注意的是分站的管理人和仓库都是一对一，我们首先需要查询是否有其他分站有相同的管理人或者仓库
        //如果有，就不能添加
        QueryWrapper<Substation> userEq = new QueryWrapper<Substation>().eq("user_id", substation.getUserId());
        if (substationService.getOne(userEq) != null) return AjaxResult.error("该管理人已经管理了其他分站");
        //检查是否存在相同地址
        QueryWrapper<Substation> addressEq = new QueryWrapper<Substation>().eq("address", substation.getAddress());
        if (substationService.getOne(addressEq) != null) return AjaxResult.error("同地址下已有分站");
        //不允许更新子站
        Substation byId = substationService.getById(substation.getId());
        if (!Objects.equals(byId.getSubwareId(), substation.getSubwareId())) return AjaxResult.error("不允许更新子站");
        substationService.updateById(substation);
        return AjaxResult.success("更新成功");
    }

    @PostMapping("/delete/{id}")
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
}

