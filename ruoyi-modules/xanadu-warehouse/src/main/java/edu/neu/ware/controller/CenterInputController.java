package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.ware.entity.CenterInput;
import edu.neu.ware.entity.CenterStorageRecord;
import edu.neu.ware.feign.CCOrderClient;
import edu.neu.ware.feign.PurchaseRecordClient;
import edu.neu.ware.service.CenterInputService;
import edu.neu.ware.service.CenterStorageRecordService;
import edu.neu.ware.vo.CenterInputVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/ware/centerInput")
@Api(tags = "CenterInputController", description = "中心仓库入库记录管理")
@Transactional
public class CenterInputController {


    @Autowired
    CenterInputService centerInputService;

    @Autowired
    PurchaseRecordClient purchaseRecordClient;

    @Autowired
    CCOrderClient ccOrderClient;


    @Autowired
    CenterStorageRecordService centerStorageRecordService;


    @PostMapping("/feign/add")
    @ApiOperation("添加入库记录")
    public Boolean add(@RequestBody CenterInputVo centerInputVo) {
        if (centerInputVo == null) return false;
        CenterInput centerInput = new CenterInput();
        BeanUtils.copyProperties(centerInputVo, centerInput);
        return centerInputService.save(centerInput);
    }


    @PutMapping("/confirm/{id}")
    @ApiOperation("确认入库,并根据入库的类型更新状态")
    public AjaxResult confirm(@PathVariable("id") Long id) {
        if (id == null) return AjaxResult.error("id不能为空");
        //入库单不存在
        CenterInput centerInput = centerInputService.getById(id);
        if (centerInput == null) return AjaxResult.error("入库单不存在");
        //如果入库单的类型为购货入库，那么需要拿到对应的采购单实体类，获取到对应的缺货ID，然后调用远程方法更新

        if (centerInput.getInputType().equals(InputOutputType.PURCHASE)) {
            //拿到sourceId
            Long sourceId = centerInput.getSourceId();
            //远程调用获取到缺货单对应的缺货记录ID
            List<Long> lackIds = purchaseRecordClient.getLackIdsAndUpdate(sourceId);
            if (lackIds == null || lackIds.size() == 0) return AjaxResult.error("入库错误,入库对应缺货单不存在");
            //更新缺货状态
            Boolean aBoolean = ccOrderClient.updateLackRecordStatusToArrival(centerInput.getInputNum(), lackIds);

            if (aBoolean == null || !aBoolean) return AjaxResult.error("入库错误,更新缺货记录失败");
        }

        //入库，增加可分配库存,可能查不到这个商品，此时就需要创建商品
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(new QueryWrapper<CenterStorageRecord>().eq("product_id", centerInput.getProductId()));

        CenterStorageRecord record = null;
        if (centerStorageRecord == null) record = centerStorageRecordService.createProduct(centerInput);
        if (record == null) return AjaxResult.error("入库失败，创建商品失败");

        //更新库存
        record.setAllocateAbleNum(record.getAllocateAbleNum() + centerInput.getInputNum());
        record.setTotalNum(record.getTotalNum() + centerInput.getInputNum());
        boolean update = centerStorageRecordService.updateById(record);
        if (!update) return AjaxResult.error("入库失败，更新库存失败");

        return AjaxResult.success("入库成功");
    }


}

