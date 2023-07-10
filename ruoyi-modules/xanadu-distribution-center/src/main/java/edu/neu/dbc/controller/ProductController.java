package edu.neu.dbc.controller;


import cn.hutool.db.sql.Order;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dbc.entity.Categary;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.feign.OrderClient;
import edu.neu.dbc.service.CategaryService;
import edu.neu.dbc.service.ProductService;
import edu.neu.dbc.service.SupplierService;
import edu.neu.dbc.vo.InfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/dbc/product")
@Api(tags = "商品管理")
@CacheConfig(cacheNames = "product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategaryService categaryService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    OrderClient orderClient;

    @Autowired
    RestTemplate restTemplate;


    /*
     * 获取列表,分页查询
     * */
    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("获取商品列表,分页查询")
    public AjaxResult list(@PathVariable Long pageNum, @PathVariable Long pageSize) {
        Page<Product> page = productService.page(new Page<>(pageNum, pageSize));
        page.getRecords().forEach(
                product -> {
                    //填充分类名称
                    Categary firstCategary = categaryService.getById(product.getFirstCategray());
                    Categary secondCategary = categaryService.getById(product.getSecondCategray());
                    product.setFirstName(firstCategary.getCategory());
                    product.setSecondName(secondCategary.getCategory());
                }
        );
        return AjaxResult.success(page);
    }

    /*
     * 获取列表,分页查询
     * */
    @GetMapping("/query/{pageNum}/{pageSize}")
    @ApiOperation("获取供应商商品列表")
    public AjaxResult query(@PathVariable Long pageNum, @PathVariable Long pageSize, @RequestParam("supplierId") String supplierId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(supplierId != null, "supplier_id", supplierId);
        Page<Product> page = productService.page(new Page<>(pageNum, pageSize));
        return AjaxResult.success(page);
    }

    @GetMapping("/listAll")
    @ApiOperation("获取所有商品")
    @Cacheable(key = "'listAll'")
    @ApiResponse(code = 200, message = "获取成功")
    public AjaxResult listAll() {
        List<Product> products = productService.list();
        products.forEach(product -> {
            Categary firstCategary = categaryService.getById(product.getFirstCategray());
            Categary secondCategary = categaryService.getById(product.getSecondCategray());
            product.setFirstName(firstCategary.getCategory());
            product.setSecondName(secondCategary.getCategory());
        });
        return AjaxResult.success(products);
    }

    @GetMapping("/crawler/{keyword}")
    @ApiOperation("爬虫爬取商品数据")
    @SuppressWarnings("all")
    public Map crawler(@ApiParam("商品关键字") @PathVariable String keyword) {
        keyword = keyword.trim();
        String url = "http://localhost:8299/xanadu/search/" + keyword;
        ResponseEntity<String> getResult = restTemplate.getForEntity(url, String.class);
        Map map = null;
        try {
            map = JSON.parseObject(getResult.getBody(), Map.class);
        } catch (Exception e) {
        }
        return map;
    }

    @PostMapping("/add")
    @ApiOperation("添加商品")
    @CacheEvict(key = "'listAll'")
    public AjaxResult add(@RequestBody Product product) {
        //需要校验，1.不为空，2.供应商ID存在
        Supplier supplier = supplierService.getById(product.getSupplierId());
        if (supplier == null) return AjaxResult.error("供应商不存在");
        //不允许商品名为空
        if (product.getName() == null) return AjaxResult.error("商品名不能为空");
        //不允许商品名重复
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>().eq("name", product.getName());
        Product one = productService.getOne(queryWrapper);
        if (one != null) return AjaxResult.error("商品名重复");
        //不允许分类为空
        if (product.getFirstCategray() == null || product.getSecondCategray() == null) {
            return AjaxResult.error("分类不能为空");
        }
        //不允许商品价格空
        if (product.getPrice() == null) return AjaxResult.error("商品价格不能为空");
        //最大库存至少为安全库存两倍
        if (product.getMaxCount() < product.getSafeStock() * 2) {
            return AjaxResult.success("最大库存至少为安全库存两倍");
        }
        boolean saved = productService.save(product);
        if (saved) {
            return AjaxResult.success(product);
        }
        return AjaxResult.error("商品添加失败");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("获取商品")
    @Cacheable(key = "'get'+#id")
    public AjaxResult get(@PathVariable Integer id) {
        Product product = productService.getById(id);
        if (product == null) {
            return AjaxResult.error("商品不存在");
        }
        Categary firstCategary = categaryService.getById(product.getFirstCategray());
        Categary secondCategary = categaryService.getById(product.getSecondCategray());
        product.setFirstName(firstCategary.getCategory());
        product.setSecondName(secondCategary.getCategory());
        return AjaxResult.success(product);
    }

    @PostMapping("/update")
    @ApiOperation("更新商品")
    @Caching(evict = {
            @CacheEvict(key = "'get'+#product.id"),
            @CacheEvict(key = "'listAll'")
    })
    public AjaxResult update(@RequestBody Product product) {
        Supplier supplier = supplierService.getById(product.getSupplierId());
        if (supplier == null) return AjaxResult.error("供应商不存在");
        boolean updated = productService.updateById(product);
        if (updated) {
            return AjaxResult.success("商品更新成功");
        }
        return AjaxResult.error("商品更新失败");
    }

    @GetMapping("/delete/{id}")
    @ApiOperation("删除商品")
    @Caching(evict = {
            @CacheEvict(key = "'get'+#id"),
            @CacheEvict(key = "'listAll'")
    })
    public AjaxResult delete(@PathVariable Long id) {
        //删除前需要检查是否已经购买过了，如果存在订单则不允许删除
        AjaxResult ajaxResult = orderClient.checkDeleteProduct(id);
        if (ajaxResult.isError()) return ajaxResult;
        boolean deleted = productService.removeById(id);
        if (deleted) {
            return AjaxResult.success("商品删除成功");
        }
        return AjaxResult.error("商品删除失败");
    }

}

