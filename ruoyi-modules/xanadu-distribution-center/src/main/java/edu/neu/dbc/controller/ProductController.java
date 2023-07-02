package edu.neu.dbc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.service.CategaryService;
import edu.neu.dbc.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>

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

    /*
     * 获取列表,分页查询
     * */
    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("获取商品列表,分页查询")
    public AjaxResult list(@PathVariable Long pageNum, @PathVariable Long pageSize) {
        Page<Product> page = productService.page(new Page<>(pageNum,pageSize));
        return AjaxResult.success(page);
    }

    @GetMapping("/listAll")
    @ApiOperation("获取所有商品")
    @Cacheable(key = "'listAll'")
    @ApiResponse(code = 200, message = "获取成功")
    public AjaxResult listAll() {
        return AjaxResult.success(productService.list());
    }


    @PostMapping("/add")
    @ApiOperation("添加商品")
    @CacheEvict(key = "'listAll'")
    public AjaxResult add(@RequestBody Product product) {
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
        return AjaxResult.success(product);
    }

    @PostMapping("/update")
    @ApiOperation("更新商品")
    @Caching(evict = {
            @CacheEvict(key = "'get'+#product.id"),
            @CacheEvict(key = "'listAll'")
    })
    public AjaxResult update(@RequestBody Product product) {
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
    public AjaxResult delete(@PathVariable Integer id) {
        boolean deleted = productService.removeById(id);
        if (deleted) {
            return AjaxResult.success("商品删除成功");
        }
        return AjaxResult.error("商品删除失败");
    }

}

