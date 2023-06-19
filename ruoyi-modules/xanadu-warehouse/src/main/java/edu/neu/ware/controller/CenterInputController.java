package edu.neu.ware.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
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
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    //TODO：入库分为两种，购货入库与退货入库，都需要提供列表方法,前端需要根据以下的字段进行构建
    //购货入库 ——  购货单号 、商品代 码、商品名称、 入库数量、日期 ，入库状态，供应商ID
    //退货入库 ——  退货单号，商品分类、 商品代码、商品名称、退货数量、分站ID，分库ID,日期，入库状态
    @GetMapping("/list/{type}")
    @ApiOperation("获取入库列表")
    public AjaxResult getInputList(@ApiParam(name = "type", value = "入库类型") String type) {
        if (type == null) return AjaxResult.error("入库类型不能为空");
        if (!type.equals(InputOutputType.PURCHASE) && !type.equals(InputOutputType.RETURN))
            throw new ServiceException("类型错误");
        List<CenterInput> centerInputs = centerInputService.list(new QueryWrapper<CenterInput>().eq("input_type", type));
        return AjaxResult.success(centerInputs);
    }


    @PostMapping("/feign/add")
    @ApiOperation("添加入库记录")
    public Boolean add(@RequestBody CenterInputVo centerInputVo) {
        if (centerInputVo == null) return false;
        CenterInput centerInput = new CenterInput();
        BeanUtils.copyProperties(centerInputVo, centerInput);
        return centerInputService.save(centerInput);
    }


    @PutMapping("/confirm/{id}/{number}")
    @ApiOperation("确认入库,并根据入库的类型更新状态,输入实际的入库量")
    public AjaxResult confirm(@PathVariable("id") Long id, @PathVariable("number") Integer number) {

        if (number == null || number <= 0) return AjaxResult.error("入库数量错误");

        if (id == null) return AjaxResult.error("id不能为空");
        //入库单不存在
        CenterInput centerInput = centerInputService.getById(id);
        if (centerInput == null) return AjaxResult.error("入库单不存在");
        //如果入库单的类型为购货入库，那么需要拿到对应的采购单实体类，获取到对应的缺货ID，然后调用远程方法更新

        if (centerInput.getInputType().equals(InputOutputType.PURCHASE)) {
            //拿到sourceId
            Long inputId = centerInput.getInputId();
            //远程调用获取到缺货单对应的缺货记录ID
            List<Long> lackIds = purchaseRecordClient.getLackIdsAndUpdate(inputId);
            if (lackIds == null || lackIds.size() == 0) return AjaxResult.error("入库错误,入库对应缺货单不存在");
            //更新缺货状态
            Boolean aBoolean = ccOrderClient.updateLackRecordStatusToArrival(centerInput.getInputNum(), lackIds);

            if (aBoolean == null || !aBoolean) return AjaxResult.error("入库错误,更新缺货记录失败");
        }

        //入库，增加可分配库存,可能查不到这个商品，此时就需要创建商品
        CenterStorageRecord centerStorageRecord = centerStorageRecordService.getOne(new QueryWrapper<CenterStorageRecord>().eq("product_id", centerInput.getProductId()));

        if (centerStorageRecord == null) centerStorageRecord = centerStorageRecordService.createProduct(centerInput);
        if (centerStorageRecord == null) return AjaxResult.error("入库失败，创建商品失败");

        //更新库存，此时需要判断来源，如果是采购的，我们需要锁定库存，如果是退货入库的，我们需要增加可分配库存
        if (centerInput.getInputType().equals(InputOutputType.PURCHASE)) {
            centerStorageRecord.setLockNum(centerStorageRecord.getLockNum() + number);
        } else {
            centerStorageRecord.setAllocateAbleNum(centerStorageRecord.getAllocateAbleNum() + number);
        }
        centerStorageRecord.setTotalNum(centerStorageRecord.getTotalNum() + number);
        boolean update = centerStorageRecordService.updateById(centerStorageRecord);
        if (!update) return AjaxResult.error("入库失败，更新库存失败");

        centerInput.setStatus(CenterInput.INPUT_STATUS_YES);
        centerInput.setActualNum(number);
        centerInput.setInputTime(new Date());

        centerInputService.updateById(centerInput);
        return AjaxResult.success("确认入库成功");
    }


}

