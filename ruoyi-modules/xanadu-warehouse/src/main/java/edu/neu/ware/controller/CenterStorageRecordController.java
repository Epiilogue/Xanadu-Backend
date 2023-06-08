package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;

import edu.neu.ware.entity.CenterStorageRecord;
import edu.neu.ware.service.CenterStorageRecordService;
import edu.neu.ware.vo.ProductRecordsVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/feign/check")
    @ApiOperation("输入多件商品ID，检查是否缺货,并返回对应的缺货数量")
    @ApiResponse(code = 200, message = "返回每个商品对应的缺货数量")
    @ApiParam(name = "productIdNumberMap", value = "商品ID和数量的键值对")
    public ProductRecordsVo check(@RequestBody Map<Long, Integer> productIdMap) {
        //首先需要根据ID 查出所有的商品的可分配数量，并根据可分配数量确定是否缺货
        //如果缺货，返回对应缺货商品的ID和缺货数量
        //如果不缺货，返回null
        if (productIdMap == null || productIdMap.size() == 0) return null;
        ProductRecordsVo productRecordsVo = new ProductRecordsVo();
        List<CenterStorageRecord> centerStorageRecords = centerStorageRecordService.listByIds(new ArrayList<>(productIdMap.keySet()));
        centerStorageRecords.forEach(centerStorageRecord -> {
            if (centerStorageRecord.getAllocateAbleNum() < productIdMap.get(centerStorageRecord.getProductId())) {
                productRecordsVo.addProductRecord(centerStorageRecord.getProductId(), productIdMap.get(centerStorageRecord.getProductId()) - centerStorageRecord.getAllocateAbleNum());
            }
        });
        //如果没有记录被添加到缺货map则说明不缺货
        productRecordsVo.setIsLack(productRecordsVo.getProductIdNumberMap().size() != 0);
        return productRecordsVo;
    }


    //获取列表
    @GetMapping("/list")
    @ApiOperation("获取中心仓库中的所有商品库存")
    public AjaxResult list() {
        List<CenterStorageRecord> centerStorageRecords = centerStorageRecordService.list();
        return AjaxResult.success(centerStorageRecords);
    }


    @GetMapping("/feign/getStorage/{productId}")
    @ApiOperation("获取商品总库存")
    @ApiParam(name = "productId", value = "商品ID")
    public Integer getStorage(@PathVariable("productId") Long productId) {
        if (productId == null) {
            return null;//商品ID不能为空
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null) {
            return 0;//中心仓库中不存在该商品
        }
        return centerStorageRecord.getTotalNum();
    }


    @PutMapping("/feign/lock/{productId}/{num}")
    @ApiOperation("锁定商品库存")
    public Boolean lock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num) {
        if (productId == null) {
            return null;//商品ID不能为空
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null) {
            return false;//中心仓库中不存在该商品,锁定失败
        }
        UpdateWrapper<CenterStorageRecord> updateWrapper = new UpdateWrapper<CenterStorageRecord>()
                .setSql("lock_num=lock_num+" + num).setSql("allocate_able_num=allocate_able_num-" + num)
                .ge("allocate_able_num", num).eq("id", centerStorageRecord.getId());
        return centerStorageRecordService.update(updateWrapper);
    }


    @PutMapping("/feign/unlock/{productId}/{num}")
    @ApiOperation("锁定商品库存")
    public Boolean unlock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num) {
        if (productId == null) {
            return null;//商品ID不能为空
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null) {
            return false;//中心仓库中不存在该商品,解锁失败
        }
        UpdateWrapper<CenterStorageRecord> updateWrapper = new UpdateWrapper<CenterStorageRecord>()
                .setSql("lock_num=lock_num-" + num).setSql("allocate_able_num=allocate_able_num+" + num)
                .ge("lock_num", num).eq("id", centerStorageRecord.getId());
        return centerStorageRecordService.update(updateWrapper);
    }


}

