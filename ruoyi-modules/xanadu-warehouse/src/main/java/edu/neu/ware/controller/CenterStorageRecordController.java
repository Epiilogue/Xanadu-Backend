package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;

import edu.neu.ware.entity.CenterStorageRecord;
import edu.neu.ware.service.CenterStorageRecordService;
import edu.neu.ware.vo.ProductRecordsVo;
import edu.neu.ware.vo.StorageVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.val;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class CenterStorageRecordController {

    @Autowired
    CenterStorageRecordService centerStorageRecordService;

    @Autowired
    RedissonClient redissonClient;//分布式锁

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

    @PostMapping("/feign/check")
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
                productRecordsVo.addProductRecord(centerStorageRecord.getProductId(),
                        productIdMap.get(centerStorageRecord.getProductId()) - centerStorageRecord.getAllocateAbleNum());
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

    //获取列表
    @GetMapping("/list/{pageNum}/{pageSize}")
    @ApiOperation("获取中心仓库中的所有商品库存")
    public AjaxResult pageList(@PathVariable Long pageNum,@PathVariable Long pageSize) {
        return AjaxResult.success(centerStorageRecordService.page(new Page<>(pageNum, pageSize)));
    }

    @GetMapping("/feign/getTotalStorage/{productId}")
    @ApiOperation("获取商品总库存")
    @ApiParam(name = "productId", value = "商品ID")
    public Integer getTotalStorage(@PathVariable("productId") Long productId) {
        if (productId == null) {
            return null;//商品ID不能为空
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null)
            return 0;//中心仓库中不存在该商品
        return centerStorageRecord.getTotalNum();
    }


    @PutMapping("/feign/lock/{productId}/{num}")
    @ApiOperation("锁定商品库存")
    public Boolean lock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num) {
        if (productId == null) {
            return null;//商品ID不能为空
        }
        RLock lock = redissonClient.getLock("lock:" + productId);
        try {
            val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
            CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
            if (centerStorageRecord == null) {
                return false;//中心仓库中不存在该商品,锁定失败
            }
            UpdateWrapper<CenterStorageRecord> updateWrapper = new UpdateWrapper<CenterStorageRecord>()
                    .setSql("lock_num=lock_num+" + num).setSql("allocate_able_num=allocate_able_num-" + num)
                    .ge("allocate_able_num", num).eq("id", centerStorageRecord.getId());
            return centerStorageRecordService.update(updateWrapper);
        } finally {
            lock.unlock();
        }

    }


    @PutMapping("/feign/unlock/{productId}/{num}")
    @ApiOperation("解锁商品库存")
    public Boolean unlock(@PathVariable("productId") Long productId, @PathVariable("num") Integer num) {
        if (productId == null) {
            return null;//商品ID不能为空
        }
        RLock lock = redissonClient.getLock("lock:" + productId);
        try {
            val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
            CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
            if (centerStorageRecord == null) {
                return false;//中心仓库中不存在该商品,解锁失败
            }
            UpdateWrapper<CenterStorageRecord> updateWrapper = new UpdateWrapper<CenterStorageRecord>()
                    .setSql("lock_num=lock_num-" + num).setSql("allocate_able_num=allocate_able_num+" + num)
                    .ge("lock_num", num).eq("id", centerStorageRecord.getId());
            return centerStorageRecordService.update(updateWrapper);
        } finally {
            lock.unlock();
        }
    }

    @PutMapping("/feign/dispatch/{productId}/{num}/{from}")
    @ApiOperation("分配商品库存,将商品库存由锁定的数量添加到已分配的数量中")
    public AjaxResult dispatch(@PathVariable("productId") Long productId, @PathVariable("num") Integer
            num, @PathVariable("from") String from) {
        if (productId == null) {
            return AjaxResult.error("商品ID不能为空");
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null) {
            return AjaxResult.error("中心仓库中不存在该商品,分配失败");
        }
        RLock lock = redissonClient.getLock("lock:" + productId);
        try {
            UpdateWrapper<CenterStorageRecord> updateWrapper = null;
            if (from.equals("lock")) {
                updateWrapper = new UpdateWrapper<CenterStorageRecord>()
                        .setSql("lock_num=lock_num-" + num).setSql("allocated_num=allocated_num+" + num)
                        .ge("lock_num", num).eq("id", centerStorageRecord.getId());
            } else if (from.equals("unlock")) {
                updateWrapper = new UpdateWrapper<CenterStorageRecord>()
                        .setSql("allocate_able_num=allocate_able_num-" + num).setSql("allocated_num=allocated_num+" + num)
                        .ge("allocate_able_num", num).eq("id", centerStorageRecord.getId());
            }
            boolean update = centerStorageRecordService.update(updateWrapper);
            if (update) return AjaxResult.success("分配成功");
            else throw new ServiceException("分配失败");
        } finally {
            lock.unlock();
        }
    }

    @GetMapping("/feign/getStorage/{productId}")
    @ApiOperation("获取商品各类库存信息")
    public StorageVo getStorage(@PathVariable("productId") Long productId) {
        if (productId == null) {
            return null;//商品ID不能为空
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null) {
            return new StorageVo();//中心仓库中不存在该商品
        }

        RLock lock = redissonClient.getLock("lock:" + productId);
        try {
            StorageVo storageVo = new StorageVo();
            storageVo.setTotalNum(centerStorageRecord.getTotalNum());
            storageVo.setLockedNum(centerStorageRecord.getLockNum());
            storageVo.setReturnedNum(centerStorageRecord.getRefundNum());
            storageVo.setAvailableNum(centerStorageRecord.getAllocateAbleNum());
            storageVo.setAllocatedNum(centerStorageRecord.getAllocatedNum());
            return storageVo;
        } finally {
            lock.unlock();
        }
    }


    @PutMapping("/feign/reDispatch/{productId}/{prevNum}/{nowNum}")
    @ApiOperation("回滚商品分配库存")
    AjaxResult reDispatch(@PathVariable("productId") Long productId, @PathVariable("prevNum") Integer
            prevNum, @PathVariable("nowNum") Integer nowNum) {
        if (productId == null) {
            return AjaxResult.error("商品ID不能为空");
        }
        val queryMapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(queryMapper);
        if (centerStorageRecord == null) {
            return AjaxResult.error("中心仓库中不存在该商品,撤回分配库存失败");
        }
        RLock lock = redissonClient.getLock("lock:" + productId);
        try {
            UpdateWrapper<CenterStorageRecord> rollbackWrapper = new UpdateWrapper<CenterStorageRecord>()
                    .setSql("allocated_num=allocated_num-" + prevNum).setSql("allocate_able_num=allocate_able_num+" + prevNum)
                    .ge("allocated_num", prevNum).eq("id", centerStorageRecord.getId());
            boolean update = centerStorageRecordService.update(rollbackWrapper);
            if (!update) throw new ServiceException("撤回分配库存失败");

            UpdateWrapper<CenterStorageRecord> updateWrapper = new UpdateWrapper<CenterStorageRecord>()
                    .setSql("allocate_able_num=allocate_able_num-" + nowNum).setSql("allocated_num=allocated_num+" + nowNum)
                    .ge("allocate_able_num", nowNum).eq("id", centerStorageRecord.getId());
            update = centerStorageRecordService.update(updateWrapper);
            if (update) return AjaxResult.success("调度成功");
            else throw new ServiceException("调度失败");
        } finally {
            lock.unlock();
        }
    }


}

