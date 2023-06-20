package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputStatus;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.ware.entity.SubOutput;
import edu.neu.ware.entity.SubStorageRecord;
import edu.neu.ware.service.SubOutputService;
import edu.neu.ware.service.SubStorageRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
@RequestMapping("/ware/subStorageRecord")
@Transactional
public class SubStorageRecordController {
    //分站记录查看
    @Autowired
    SubStorageRecordService subStorageRecordService;

    @Autowired
    SubOutputService subOutputService;

    @GetMapping("/list")
    @ApiOperation(value = "分站记录查看")
    public AjaxResult list() {
        return AjaxResult.success(subStorageRecordService.list());
    }


    @GetMapping("/feign/check")
    @ApiOperation(value = "检查商品是否数量都充足")
    AjaxResult check(@RequestParam("subwareId") Long subwareId, @RequestBody HashMap<Long, Integer> longIntegerHashMap) {
        List<Long> idList = new ArrayList<>(longIntegerHashMap.keySet());
        List<SubStorageRecord> list = subStorageRecordService.list(new QueryWrapper<SubStorageRecord>().eq("subware_id", subwareId).in("product_id", idList));
        //拿到所有的记录
        if (list.size() < idList.size()) {
            //如果记录数小于商品数，说明有商品没有记录
            return AjaxResult.error("商品可分配数量不足");
        }
        //对于每一个商品比较一下库存
        for (SubStorageRecord subStorageRecord : list) {
            if (subStorageRecord.getAllocateAbleNum() < longIntegerHashMap.get(subStorageRecord.getProductId())) {
                return AjaxResult.error("商品可分配数量不足");
            }
        }
        return AjaxResult.success(true);
    }

    @PostMapping("/feign/lock")
    @ApiOperation(value = "锁定库存")
    AjaxResult lock(@RequestParam("subwareId") Long subwareId, @RequestBody HashMap<Long, Integer> longIntegerHashMap) {
        for (Map.Entry<Long, Integer> longIntegerEntry : longIntegerHashMap.entrySet()) {
            boolean success = subStorageRecordService.update(new UpdateWrapper<SubStorageRecord>().
                    eq("subware_id", subwareId).eq("product_id", longIntegerEntry.getKey())
                    .setSql("allocated_num=allocated_num+" + longIntegerEntry.getValue()).
                    setSql("allocate_able_num=allocate_able_num-" + longIntegerEntry.getValue()).
                    gt("allocate_able_num", longIntegerEntry.getValue()));
            if (!success) throw new RuntimeException("分配商品库存失败");
        }
        return AjaxResult.success(true);
    }

    @PostMapping("/ware/subStorageRecord/feign/reduce")
    @ApiOperation(value = "减少库存")
    AjaxResult reduce(@RequestParam("subwareId") Long subwareId, @RequestParam("taskId") Long taskId, @RequestBody HashMap<Long, Integer> longIntegerHashMap) {
        for (Map.Entry<Long, Integer> longIntegerEntry : longIntegerHashMap.entrySet()) {
            //拿到商品名
            String productName = subStorageRecordService.getById(longIntegerEntry.getKey()).getProductName();
            boolean success = subStorageRecordService.update(new UpdateWrapper<SubStorageRecord>().
                    eq("subware_id", subwareId).eq("product_id", longIntegerEntry.getKey())
                    .setSql("allocated_num=allocated_num-" + longIntegerEntry.getValue()).
                    setSql("total_num=total_num-" + longIntegerEntry.getValue()));
            if (!success) throw new RuntimeException("分配商品库存失败");
            //生成出库单
            SubOutput subOutput = new SubOutput(null, taskId, longIntegerEntry.getKey(), productName, longIntegerEntry.getValue(),
                    InputOutputType.PICK_OUT, new Date(), subwareId, false, InputOutputStatus.OUTPUT, longIntegerEntry.getValue());
            boolean save = subOutputService.save(subOutput);
            if (!save) throw new RuntimeException("生成出库记录失败");
        }
        return AjaxResult.success(true);
    }

}

