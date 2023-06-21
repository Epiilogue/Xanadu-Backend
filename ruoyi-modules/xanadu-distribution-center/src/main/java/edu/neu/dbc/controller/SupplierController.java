package edu.neu.dbc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.service.SupplierService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@RestController
@RequestMapping("/dbc/supplier")
public class SupplierController {

    //列表方法以及根据ID查信息
    //供应商增删改查

    @Autowired
    SupplierService supplierService;


    @GetMapping("/list")
    @ApiOperation("查询所有供应商")
    public AjaxResult list() {
        return AjaxResult.success(supplierService.list());
    }

    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("分页查询供应商")
    public AjaxResult pageList(@PathVariable Long pageNum, @PathVariable Long pageSize) {
        return AjaxResult.success(supplierService.page(new Page<>(pageNum,pageSize)));
    }

    @GetMapping("/query/{pageNum}/{pageSize}")
    @ApiOperation("根据姓名、地址查询供应商")
    public AjaxResult query(@PathVariable Long pageNum, @PathVariable Long pageSize,@RequestParam(value = "name" ,required=false) String name,@RequestParam(value = "address" ,required=false) String address) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.like(name!=null,"name",name).like(address!=null,"address",address);
        return AjaxResult.success(supplierService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    @PostMapping("/add")
    @ApiOperation("添加供应商")
    public AjaxResult add(@RequestBody Supplier supplier) {
        return AjaxResult.success(supplierService.save(supplier));
    }

    @PostMapping("/update")
    @ApiOperation("更新供应商")
    public AjaxResult update(@RequestBody Supplier supplier) {
        return AjaxResult.success(supplierService.updateById(supplier));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除供应商")
    public AjaxResult delete(@PathVariable Long id) {
        //TODO: 检查供应商是否有过采购记录
        return AjaxResult.success(supplierService.removeById(id));
    }

    //根据ID查询名字
    @GetMapping("/feign/getSupplierNames")
    @ApiOperation("获取所有供应商的id和名字")
    public Map<Long, String> getSupplierNames() {
        HashMap<Long, String> longStringHashMap = new HashMap<>();
        supplierService.list().forEach(
                s -> longStringHashMap.put(s.getId(), s.getName())
        );
        return longStringHashMap;
    }


}
