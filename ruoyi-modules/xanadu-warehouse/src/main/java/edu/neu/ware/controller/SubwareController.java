package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.domain.AjaxResult;

import edu.neu.ware.entity.SubStorageRecord;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.feign.CCOrderClient;
import edu.neu.ware.service.SubStorageRecordService;
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

    //分库的增加,前端需要选择好城市等信息，还需要选择库管员
    //修改，允许修改地址等，
    //删除，即仓库无货物并且没有未完成的订单或者任务单才能够删除
    @Autowired
    private SubwareService subwareService;

    @Autowired
    private CCOrderClient ccOrderClient;

    @Autowired
    private SubStorageRecordService subStorageRecordService;

    @PostMapping("/add")
    @ApiOperation(value = "添加分库", notes = "添加分库")
    public AjaxResult addSubware(@RequestBody Subware subware) {

        String valitedResult = subwareService.validateSubware(subware);
        if (valitedResult != null) return AjaxResult.error(valitedResult);

        boolean res = subwareService.save(subware);
        if (!res) return AjaxResult.error("创建分库失败");
        return AjaxResult.success("创建分库成功", subware);
    }

    /**
     * 1.获取所有可分配的库管员
     * 2.批量添加库管员
     * 3.删除库管员
     * 4.查看库管员
     */


    @PostMapping("/update")
    @ApiOperation(value = "修改分库", notes = "修改分库")
    public AjaxResult updateSubware(@RequestBody Subware subware) {

        String valitedResult = subwareService.validateSubware(subware);
        if (valitedResult != null) return AjaxResult.error(valitedResult);

        boolean res = subwareService.updateById(subware);
        if (!res) return AjaxResult.error("修改分库失败");
        return AjaxResult.success("修改分库成功", subware);
    }

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除分库", notes = "删除分库")
    public AjaxResult deleteSubware(@PathVariable Long id) {
        Subware subware = subwareService.getById(id);
        if (subware == null) return AjaxResult.error("分库不存在");

        QueryWrapper<SubStorageRecord> totalNum = new QueryWrapper<SubStorageRecord>().gt("total_num", 0);
        List<SubStorageRecord> list = subStorageRecordService.list(totalNum);
        if (list.size() > 0) return AjaxResult.error("分库有货物，不能删除");

        boolean res = subwareService.removeById(id);
        if (!res) return AjaxResult.error("删除分库失败");
        return AjaxResult.success("删除分库成功", subware);
    }

    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "获取分库列表", notes = "获取分库列表")
    public AjaxResult getSubwareList(@PathVariable Integer page, @PathVariable Integer size) {
        List<Subware> subwareList = subwareService.page(new Page<>(page, size)).getRecords();
        if (subwareList == null) return AjaxResult.error("获取分库列表失败");
        return AjaxResult.success("获取分库列表成功", subwareList);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取分库", notes = "获取分库")
    public AjaxResult getSubware(@PathVariable Long id) {
        Subware subware = subwareService.getById(id);
        if (subware == null) return AjaxResult.error("获取分库失败");
        return AjaxResult.success("获取分库成功", subware);
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有分库", notes = "获取所有分库")
    public AjaxResult getAllSubware() {
        List<Subware> subwareList = subwareService.list();
        if (subwareList == null) return AjaxResult.error("获取分库列表失败");
        return AjaxResult.success("获取分库列表成功", subwareList);
    }


}

