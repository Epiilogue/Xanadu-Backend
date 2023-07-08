package edu.neu.dbc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.entity.PurchaseRecord;
import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.service.ProductService;
import edu.neu.dbc.service.PurchaseRecordService;
import edu.neu.dbc.service.SupplierService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@CacheConfig(cacheNames = "supplier")
public class SupplierController {

    //列表方法以及根据ID查信息
    //供应商增删改查

    @Autowired
    SupplierService supplierService;

    @Autowired
    PurchaseRecordService purchaseRecordService;
    @Autowired
    ProductService productService;


    @GetMapping("/listAll")
    @ApiOperation("查询所有供应商")
    public AjaxResult listAll() {
        return AjaxResult.success(supplierService.list());
    }

    @GetMapping("/get/{id}")
    @ApiOperation("获取供应商")
    @Cacheable(key = "'get'+#id")
    public AjaxResult get(@PathVariable Integer id) {
        Supplier supplier = supplierService.getById(id);
        if (supplier == null) {
            return AjaxResult.error("供应商不存在");
        }
        return AjaxResult.success(supplier);
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
    @CacheEvict(key = "'list'")
    public AjaxResult add(@RequestBody Supplier supplier) {
        return AjaxResult.success(supplierService.save(supplier));
    }

    @PostMapping("/update")
    @ApiOperation("更新供应商")
    @Caching(evict = {
            @CacheEvict(key = "'get'+#supplier.id")
    })
    public AjaxResult update(@RequestBody Supplier supplier) {
        return AjaxResult.success(supplierService.updateById(supplier));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除供应商")
    @Caching(evict = {
            @CacheEvict(key = "'get'+#id")
    })
    public AjaxResult delete(@PathVariable Long id) {
        QueryWrapper<PurchaseRecord> purchaseRecordQueryWrapper = new QueryWrapper<PurchaseRecord>().eq("supplier_id", id);
        if (purchaseRecordService.count(purchaseRecordQueryWrapper) > 0) {
            return AjaxResult.error("该供应商有过采购记录，无法删除");
        }
        //还需要找到是否存在商品由该供货商供货，如果存在，也不能删除
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<Product>().eq("supplier_id", id);
        if (productService.count(productQueryWrapper) > 0) {
            return AjaxResult.error("该供应商有过商品供货，无法删除");
        }
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
