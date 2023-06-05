package edu.neu.cc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.cc.entity.Stockout;
import edu.neu.cc.service.StockoutService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
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


    @PutMapping("/edit")
    @ApiOperation("编辑缺货记录")
    public AjaxResult edit(@RequestBody Stockout stockout) {
        if (stockoutService.getById(stockout.getId()).getStatus().equals(StockoutConstant.UNCOMMITTED)) {
            val prevStockout = stockoutService.getById(stockout.getId());
            //只允许增加缺货数量，不允许减少
            if (stockout.getNeedNumbers() < prevStockout.getNeedNumbers()) {
                return AjaxResult.error("只允许增加缺货数量");
            }
            stockoutService.updateById(stockout);
            return AjaxResult.success("编辑成功");
        } else {
            return AjaxResult.error("只允许编辑未提交的记录");
        }
    }

    @PutMapping("/commit/{id}")
    @ApiOperation("提交缺货记录")
    public AjaxResult commit(@ApiParam("缺货记录ID") @PathVariable(value = "id", required = false) Long id) {
        val stockout = stockoutService.getById(id);
        if (stockout.getStatus().equals(StockoutConstant.UNCOMMITTED)) {
            stockout.setStatus(StockoutConstant.COMMITTED);
            stockoutService.updateById(stockout);
            return AjaxResult.success("提交成功");
        } else {
            return AjaxResult.error("只允许提交未提交的记录");
        }
    }

    @PutMapping("/arrival/{id}")
    @ApiOperation("到货")
    public AjaxResult arrival(@ApiParam("缺货记录ID") @PathVariable(value = "id", required = false) Long id) {
        val stockout = stockoutService.getById(id);
        if (stockout.getStatus().equals(StockoutConstant.COMMITTED)) {
            stockout.setStatus(StockoutConstant.ARRIVAL);
            stockoutService.updateById(stockout);
            return AjaxResult.success("到货成功");
        } else {
            return AjaxResult.error("只允许到货已提交的记录");
        }
    }


}

