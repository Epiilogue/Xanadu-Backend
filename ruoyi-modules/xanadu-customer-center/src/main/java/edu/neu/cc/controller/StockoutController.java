package edu.neu.cc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.cc.entity.Stockout;
import edu.neu.cc.service.StockoutService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@RestController
@RequestMapping("/cc/stockout")
public class StockoutController {

    @Autowired
    StockoutService stockoutService;

    //基础的分页，查询，新增，修改，删除
    //分页查询

    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("查询所有缺货登记")
    public AjaxResult list(@ApiParam("页码") @PathVariable(value = "pageNum", required = false) Integer pageNum,
                           @ApiParam("每页记录数") @PathVariable(value = "pageSize", required = false) Integer pageSize
    ) {
        if (pageNum == null || pageSize == null) return AjaxResult.success(stockoutService.list());
        Page<Stockout> page = stockoutService.page(new Page<>(pageNum, pageSize));
        return AjaxResult.success(page);
    }


}

