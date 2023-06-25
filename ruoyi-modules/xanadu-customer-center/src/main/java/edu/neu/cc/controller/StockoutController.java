package edu.neu.cc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.cc.entity.Stockout;
import edu.neu.cc.service.StockoutService;
import edu.neu.cc.vo.RecordVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @GetMapping("/listAll")
    @ApiOperation("获取所有缺货记录")
    public AjaxResult list() {
        List<Stockout> list = stockoutService.list();
        if (list == null || list.size() == 0) {
            return AjaxResult.error("没有记录");
        }
        return AjaxResult.success(list);
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


    @PostMapping("/add")
    @ApiOperation("新增缺货记录,不用选订单ID，但是需要选择商品ID,前端需要添加创建缺货记录页面，选择商品以及数量即可")
    public AjaxResult add(@RequestBody Stockout stockout) {
        stockout.setId(null);
        stockout.setDeleted(false);
        //TODO: 2023/6/1 11:20 根据上下文自动获取用户ID
        stockout.setCreateBy(1L);
        stockout.setCreateTime(new Date());
        stockout.setStatus(StockoutConstant.UNCOMMITTED);
        stockoutService.save(stockout);
        return AjaxResult.success("新增成功");
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


    @GetMapping("/feign/getAllLackRecord")
    @ApiOperation("获取所有商品缺货记录,feign远程调用专用，前端不要使用该接口")
    public List<RecordVo> getAllLackRecord() {
        //首先需要远程获取缺货的记录，要求必须为已提交
        return stockoutService.list(new QueryWrapper<Stockout>().eq("status", StockoutConstant.COMMITTED)).stream().map(
                stockout -> new RecordVo(stockout.getId(), stockout.getOrderId(), stockout.getNeedNumbers(), stockout.getCreateTime(),
                        stockout.getCreateBy(), "订单缺货")
        ).collect(Collectors.toList());
    }

    @GetMapping("/feign/getLackRecord/{productId}")
    @ApiOperation("获取缺货记录,feign远程调用专用，前端不要使用该接口")
    public List<RecordVo> getLackRecordById(
            @PathVariable(value = "productId", required = true) Long productId) {
        //首先需要远程获取缺货的记录，要求必须为已提交
        return stockoutService.list(new QueryWrapper<Stockout>().eq("status", StockoutConstant.COMMITTED).eq("product_id", productId)).stream().map(
                stockout -> new RecordVo(stockout.getId(), stockout.getOrderId(), stockout.getNeedNumbers(), stockout.getCreateTime(),
                        stockout.getCreateBy(), "订单缺货")
        ).collect(Collectors.toList());
    }


    @PostMapping("/feign/updateLackRecordStatusToArrival")
    @ApiOperation("更新缺货记录状态,feign远程调用专用，前端不要使用该接口,传递的参数id列表,实际到货数量")
    public Boolean updateLackRecordStatusToArrival(@RequestParam("number") Integer number, @RequestBody List<Long> ids) {
        //查询所有的缺货记录，然后按照需要的商品数量从小到大排序，如果入库数量足够的话就可以更新状态为已到货，否则重新置为已提交等待下一轮采购
        List<Stockout> stockouts = stockoutService.listByIds(ids);
        if (Objects.isNull(stockouts) || stockouts.size() == 0) return false;
        stockouts.sort(Comparator.comparingInt(Stockout::getNeedNumbers));
        //如果入库数量足够的话就可以更新状态为已到货，否则重新置为已提交等待下一轮采购
        for (Stockout stockout : stockouts) {
            if (number >= stockout.getNeedNumbers()) {
                stockout.setStatus(StockoutConstant.ARRIVAL);
                stockoutService.updateById(stockout);
                number -= stockout.getNeedNumbers();
            } else {
                stockout.setStatus(StockoutConstant.COMMITTED);
                stockoutService.updateById(stockout);
            }
        }
        return true;
    }


    @PostMapping("/feign/updateLackRecordStatusToPurchased")
    @ApiOperation("更新缺货记录状态,feign远程调用专用，前端不要使用该接口,传递的参数id列表")
    public Boolean updateLackRecordStatusToPurchased(@RequestBody List<Long> ids) {
        if (ids.size() == 0) return true;
        UpdateWrapper<Stockout> set = new UpdateWrapper<Stockout>().in("id", ids).set("status", StockoutConstant.PURCHASED);
        return stockoutService.update(set);
    }

}

