package edu.neu.ware.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.domain.AjaxResult;

import edu.neu.ware.entity.Subware;
import edu.neu.ware.service.SubwareService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 03:42:21
 */
@RestController
@RequestMapping("/ware/subware")
public class SubwareController {

    //子站的增加,前端需要选择好城市等信息，还需要选择库管员
    //修改，允许修改地址等，
    //删除，即仓库无货物并且没有未完成的订单或者任务单才能够删除
    @Autowired
    private SubwareService subwareService;

    @PostMapping("/add")
    @ApiOperation(value = "添加子站", notes = "添加子站")
    public AjaxResult addSubware(@RequestBody Subware subware) {

        String valitedResult = subwareService.validateSubware(subware);
        if (valitedResult != null) return AjaxResult.error(valitedResult);

        boolean res = subwareService.save(subware);
        if (!res) return AjaxResult.error("创建子站失败");
        return AjaxResult.success("创建子站成功", subware);
    }


    @PostMapping("/update")
    @ApiOperation(value = "修改子站", notes = "修改子站")
    public AjaxResult updateSubware(@RequestBody Subware subware) {

        String valitedResult = subwareService.validateSubware(subware);
        if (valitedResult != null) return AjaxResult.error(valitedResult);

        boolean res = subwareService.updateById(subware);
        if (!res) return AjaxResult.error("修改子站失败");
        return AjaxResult.success("修改子站成功", subware);
    }

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除子站", notes = "删除子站")
    public AjaxResult deleteSubware(@PathVariable Long id) {
        Subware subware = subwareService.getById(id);
        if (subware == null) return AjaxResult.error("子站不存在");

        //TODO:检查子站是否有未完成的订单或者任务单,如果存在则不能删除


        boolean res = subwareService.removeById(id);
        if (!res) return AjaxResult.error("删除子站失败");
        return AjaxResult.success("删除子站成功", subware);
    }

    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "获取子站列表", notes = "获取子站列表")
    public AjaxResult getSubwareList(@PathVariable Integer page, @PathVariable Integer size) {
        List<Subware> subwareList = subwareService.page(new Page<>(page, size)).getRecords();
        if (subwareList == null) return AjaxResult.error("获取子站列表失败");
        return AjaxResult.success("获取子站列表成功", subwareList);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取子站", notes = "获取子站")
    public AjaxResult getSubware(@PathVariable Long id) {
        Subware subware = subwareService.getById(id);
        if (subware == null) return AjaxResult.error("获取子站失败");
        return AjaxResult.success("获取子站成功", subware);
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有子站", notes = "获取所有子站")
    public AjaxResult getAllSubware() {
        List<Subware> subwareList = subwareService.list();
        if (subwareList == null) return AjaxResult.error("获取子站列表失败");
        return AjaxResult.success("获取子站列表成功", subwareList);
    }


}

