package edu.neu.dbc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dbc.entity.Categary;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.service.CategaryService;
import edu.neu.dbc.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@CacheConfig(cacheNames = "categary")
public class CategaryController {

    @Autowired
    private CategaryService categaryService;

    @Autowired
    private ProductService productService;


    @GetMapping("/listAll")
    @ApiOperation("获取所有分类")
    @Cacheable(key = "'listAll'")
    public AjaxResult list() {
        List<Categary> list = categaryService.listTree();
        if (list == null || list.size() == 0) {
            return AjaxResult.error("没有分类");
        }
        return AjaxResult.success(list);
    }

    @PostMapping("/add")
    @ApiOperation("添加分类")
    @CacheEvict(key = "'listAll'")
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
    @Caching(evict = {
            @CacheEvict(key = "'listAll'"),
            @CacheEvict(key = "#categary.id")
    })
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
    @Caching(evict = {
            @CacheEvict(key = "'listAll'"),
            @CacheEvict(key = "#id")
    })
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
    @Cacheable(key = "#id")
    public AjaxResult get(@PathVariable Integer id) {
        Categary categary = categaryService.getById(id);
        if (categary == null) {
            return AjaxResult.error("没有该分类");
        }
        return AjaxResult.success(categary);
    }


    @ApiOperation("根据id获取小分类下的所有商品")
    @GetMapping("/feign/getGoods/{id}")
    public AjaxResult getGoods(@PathVariable("id") Integer id) {
        Categary categary = categaryService.getById(id);
        if (categary.getLevel() == 0) return AjaxResult.error("商品分类填写不完整");
        //查找所有的同分类ID的商品列表
        QueryWrapper<Product> secondCategray = new QueryWrapper<Product>().eq("second_categray", id);
        List<Long> Idlist = productService.list(secondCategray).stream().map(Product::getId).collect(Collectors.toList());
        return AjaxResult.success(Idlist);
    }


}

