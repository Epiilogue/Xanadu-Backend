package edu.neu.dbc.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dbc.entity.Categary;
import edu.neu.dbc.service.CategaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@RestController
@RequestMapping("/dbc/categary")
public class CategaryController {

    @Autowired
    private CategaryService categaryService;

    @GetMapping("/listAll")
    @ApiOperation("获取所有分类")
    public AjaxResult list() {
        List<Categary> list = categaryService.list();
        if (list == null || list.size() == 0) {
            return AjaxResult.error("没有分类");
        }
        return AjaxResult.success(list);
    }

    @PostMapping("/add")
    @ApiOperation("添加分类")
    public AjaxResult add(@RequestBody Categary categary) {
        //前端可能同时添加多个分类，交给前端构建,后端直接保存即可
        boolean saved = categaryService.save(categary);
        if (saved) {
            return AjaxResult.success(categary);
        }
        return AjaxResult.error("添加失败");
    }

    @PostMapping("/update")
    @ApiOperation("更新分类")
    public AjaxResult update(@RequestBody Categary categary) {
        //前端可能同时添加多个分类，交给前端构建,后端直接保存即可
        boolean updated = categaryService.updateById(categary);
        if (updated) {
            return AjaxResult.success("更新成功");
        }
        return AjaxResult.error("更新失败");
    }

    @GetMapping("/delete/{id}")
    @ApiOperation("删除分类")
    public AjaxResult delete(@PathVariable Integer id) {
        //前端可能同时添加多个分类，交给前端构建,后端直接保存即可
        boolean deleted = categaryService.removeById(id);
        if (deleted) {
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }


    @ApiOperation("根据id获取分类")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable Integer id) {
        Categary categary = categaryService.getById(id);
        if (categary == null) {
            return AjaxResult.error("没有该分类");
        }
        return AjaxResult.success(categary);
    }


}

