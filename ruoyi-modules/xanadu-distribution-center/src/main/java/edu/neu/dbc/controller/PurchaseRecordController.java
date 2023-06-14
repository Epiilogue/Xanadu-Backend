package edu.neu.dbc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputStatus;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.base.constant.cc.PurchaseRecordStatusConstant;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.entity.PurchaseRecord;
import edu.neu.dbc.entity.Refund;
import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.feign.CenterWareClient;
import edu.neu.dbc.feign.OrderClient;
import edu.neu.dbc.feign.StockoutClient;
import edu.neu.dbc.feign.WareCenterStorageRecordClient;
import edu.neu.dbc.service.ProductService;
import edu.neu.dbc.service.PurchaseRecordService;
import edu.neu.dbc.service.RefundService;
import edu.neu.dbc.service.SupplierService;
import edu.neu.dbc.vo.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@RestController
@RequestMapping("/dbc/purchaseRecord")
@Transactional
public class PurchaseRecordController {
    //生成采购单，需要传入的为缺货单，生成采购单后，将所有涉及到的缺货记录状态置为已采购
    //可以修改采购数量，但是采购数量不可以小于缺货数量也不可以大于最大库存量

    @Autowired
    PurchaseRecordService purchaseRecordService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    @ApiModelProperty("远程订单服务")
    OrderClient orderClient;


    @Autowired
    @ApiModelProperty("远程缺货记录服务")
    StockoutClient stockoutClient;

    @Autowired
    CenterWareClient centerWareClient;


    @ApiOperation("列表查看采购单，后面有已到货按钮，点击后，需要填写实际到货数量，将采购单状态置为已到货，生成入库调拨单")
    @GetMapping("/list")
    public AjaxResult list() {
        List<PurchaseRecord> purchaseRecords = purchaseRecordService.list();
        return AjaxResult.success(purchaseRecords);
    }

    @ApiOperation("确认采购到货，需要填写实际的到货数量，点击后生成入库调拨单，之后需要修改采购单的实际数量以及花费，状态改为已到货")
    @PutMapping("/confirmPurchase")
    public AjaxResult confirmPurchase(@ApiParam("对应的采购单ID") @RequestParam("id") Long id, @ApiParam("实际的到货数量") @RequestParam("number") Integer number) {
        PurchaseRecord record = purchaseRecordService.getById(id);
        if (record == null) return AjaxResult.error("采购单不存在");
        if (!record.getStatus().equals(PurchaseRecordStatusConstant.PURCHASED))
            return AjaxResult.error("采购单状态非法");
        if (number == null || number <= 0) return AjaxResult.error("到货数量非法");
        //更新采购单状态
        record.setTotalCost(record.getProductPrice() * number);
        record.setNumber(number);
        record.setStatus(PurchaseRecordStatusConstant.ARRIVED);
        if (!purchaseRecordService.updateById(record)) throw new ServiceException("更新采购单状态失败");
        //生成入库调拨单
        CenterInputVo centerInputVo = new CenterInputVo(null, id, InputOutputType.PURCHASE, record.getProductId(),
                record.getProductName(), number, record.getProductPrice(), new Date(), InputOutputStatus.NOT_INPUT, record.getSupplierId(),null,null);
        //远程调用接口，保存入库调拨单，之后可以根据入库调拨单进行入库操作
        if (!centerWareClient.addInputRecord(centerInputVo)) throw new ServiceException("生成入库调拨单失败");
        return AjaxResult.success("确认采购到货成功");
    }


    @PostMapping("/generatePurchaseOrder")
    @ApiOperation("生成采购单")
    public AjaxResult generatePurchaseOrder(@ApiParam("之前得到的单条记录") @RequestBody AllLackRecordVo allLackRecordVo,
                                            @ApiParam("供销商ID") Long supplierId) {
        if (supplierId == null) return AjaxResult.error("供销商ID为空");
        Supplier supplier = supplierService.getById(supplierId);
        if (supplier == null) return AjaxResult.error("供销商不存在");
        if (allLackRecordVo == null) return AjaxResult.error("请选择需要采购的商品");
        if (allLackRecordVo.getSingleLackRecordVos().size() == 0 || allLackRecordVo.getNeedCount() == 0)
            return AjaxResult.error("缺货记录为空");
        //检查进货数量必须要大于等于缺货数量，加上当前库存数量不能大于最大库存量
        Product product = allLackRecordVo.getProduct();
        if (product == null) return AjaxResult.error("商品不存在");
        if (allLackRecordVo.getNeedCount() > allLackRecordVo.getInputCount())
            return AjaxResult.error("进货数量不能小于缺货数量");
        if (allLackRecordVo.getInputCount() + allLackRecordVo.getNowCount() > product.getMaxCount())
            return AjaxResult.error("进货数量加上当前库存数量不能大于该商品最大库存量");

        StringBuilder stringBuilder = new StringBuilder();
        //获取所有缺货记录ID，拼接为字符串保存至数据库
        allLackRecordVo.getSingleLackRecordVos().stream().
                map(SingleLackRecordVo::getId).collect(Collectors.toList()).forEach(id -> stringBuilder.append(id).append(","));


        //完成校验后，生成采购单
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setNumber(allLackRecordVo.getInputCount());
        purchaseRecord.setProductId(product.getId());
        purchaseRecord.setProductName(product.getName());
        purchaseRecord.setProductPrice(product.getPrice());
        purchaseRecord.setSupplierId(supplierId);
        purchaseRecord.setNumber(allLackRecordVo.getInputCount());
        purchaseRecord.setLackIds(stringBuilder.toString());

        //设置采购单状态
        purchaseRecord.setStatus(PurchaseRecordStatusConstant.PURCHASED);
        purchaseRecord.setTotalCost(allLackRecordVo.getInputCount() * product.getPrice());
        //插入数据库
        boolean saved = purchaseRecordService.save(purchaseRecord);
        if (!saved) throw new ServiceException("采购单生成失败");
        //TODO：更新所有的缺货记录状态，将其置为已采购，这样缺货检查就不会重复检查
        Boolean updateSuccess = stockoutClient.updateLackRecordStatusToPurchased(allLackRecordVo.getSingleLackRecordVos().stream().
                map(SingleLackRecordVo::getId).collect(Collectors.toList()));

        if (!updateSuccess) throw new ServiceException("更新缺货记录状态失败");

        return AjaxResult.success("采购单生成成功");
    }


    @ApiOperation("获取缺货记录ID列表,feign调用")
    @GetMapping("/feign/getLackIds/{purchaseId}}")
    public List<Long> getLackIdsAndUpdate(@PathVariable("purchaseId") Long purchaseId) {
        PurchaseRecord record = purchaseRecordService.getById(purchaseId);
        if (record == null) return null;
        record.setStatus(PurchaseRecordStatusConstant.IN_STORAGE);
        purchaseRecordService.updateById(record);
        return Arrays.stream(record.getLackIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
    }


}

