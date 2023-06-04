package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;

import edu.neu.ware.entity.CenterStorageRecord;
import edu.neu.ware.service.CenterStorageRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/ware/centerStorageRecord")
public class CenterStorageRecordController {

    @Autowired
    CenterStorageRecordService centerStorageRecordService;

    @GetMapping("/getCount/{productId}")
    @ApiOperation("获取商品库存")
    @ApiParam(name = "productId", value = "商品ID")
    public AjaxResult getCount(@PathVariable("productId") Long productId) {
        if (productId == null) {
            return AjaxResult.error("商品ID不能为空");
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null) {
            return AjaxResult.error("中心仓库中不存在该商品");
        }
        return AjaxResult.success(centerStorageRecord);
    }

    @GetMapping("/check")
    @ApiOperation("输入多件商品ID，检查是否缺货")
    @ApiResponse(code = 200, message = "返回每个商品对应的缺货数量")
    @ApiParam(name = "productIds", value = "商品ID数组")
    public AjaxResult check(@PathVariable("productIds") List<Long> productIds) {



    }


}

