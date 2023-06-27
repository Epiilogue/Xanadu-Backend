package edu.neu.dbc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import org.apache.commons.collections4.map.HashedMap;
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

    @Autowired
    ProductService productService;


    @Autowired
    RefundService refundService;

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
                record.getProductName(), number, record.getProductPrice(), new Date(), InputOutputStatus.NOT_INPUT, record.getSupplierId(), null, null);
        //远程调用接口，保存入库调拨单，之后可以根据入库调拨单进行入库操作
        if (!centerWareClient.addInputRecord(centerInputVo)) throw new ServiceException("生成入库调拨单失败");
        return AjaxResult.success("确认采购到货成功");
    }


    @PostMapping("/generatePurchaseOrder")
    @ApiOperation("生成采购单")
    public AjaxResult generatePurchaseOrder(@ApiParam("之前得到的单条记录") @RequestBody AllLackRecordVo allLackRecordVo) {
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
        purchaseRecord.setSupplierId(product.getSupplierId());
        purchaseRecord.setNumber(allLackRecordVo.getInputCount());
        purchaseRecord.setLackIds(stringBuilder.toString());
        purchaseRecord.setDeleted(false);
        //设置采购单状态
        purchaseRecord.setStatus(PurchaseRecordStatusConstant.PURCHASED);
        purchaseRecord.setTotalCost(allLackRecordVo.getInputCount() * product.getPrice());
        //插入数据库
        boolean saved = purchaseRecordService.save(purchaseRecord);
        if (!saved) throw new ServiceException("采购单生成失败");
        Boolean updateSuccess = stockoutClient.updateLackRecordStatusToPurchased(allLackRecordVo.getSingleLackRecordVos().stream().
                map(SingleLackRecordVo::getId).
                filter(id -> id != -1).collect(Collectors.toList()));
        if (!updateSuccess) throw new ServiceException("更新缺货记录状态失败");

        return AjaxResult.success("采购单生成成功");
    }


    @ApiOperation("获取缺货记录ID列表,feign调用")
    @GetMapping("/feign/getLackIds/{purchaseId}}")
    public List<Long> getLackIdsAndUpdate(@PathVariable("purchaseId") Long purchaseId) {
        PurchaseRecord record = purchaseRecordService.getById(purchaseId);
        if (record == null) return null;
        return Arrays.stream(record.getLackIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
    }


    /**
     * 当我们完成某一个时间点某一个商品的结算后，修改相应的采购单状态为已结算，修改相应的退货单状态为已结算
     * 我们还需要记录一下结算的时间，保存一下结算记录，以便于后续查询
     */
    @ApiOperation("供应商结算远程查询接口，允许查询指定供应商，指定商品，指定时间段的结算信息,必须要指定供应商，但是可以不用指定商品，必须指定时间段")
    @GetMapping("/feign/settlement")
    public AjaxResult settlement(@RequestParam(value = "supplierId") Long supplierId, @RequestParam(value = "productId") Long productId,
                                 @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        //检查数据合法性
        //1.检查供应商是否存在
        Supplier supplier = supplierService.getById(supplierId);
        if (supplier == null) return AjaxResult.error("供应商不存在");
        //2.检查商品是否存在
        Product product = productService.getById(productId);
        if (product == null) return AjaxResult.error("商品不存在");
        //3.检查商品和供应商是否对应
        if (!product.getSupplierId().equals(supplierId)) return AjaxResult.error("商品和供应商不对应");
        //4.检查时间合法性<
        if (startTime.after(endTime)) return AjaxResult.error("开始时间不能晚于结束时间");

        //查询时间范围内状态为开始时间到结束时间的购买记录以及退货记录，然后记录ID，在结算后需要更新他们的状态
        //同时在SettlementVo中需要记录对应的ID列表，用以追踪更新状态
        List<PurchaseRecord> purchaseRecords = purchaseRecordService.lambdaQuery().
                eq(PurchaseRecord::getSupplierId, supplierId).
                between(PurchaseRecord::getCreateTime, startTime, endTime).
                eq(PurchaseRecord::getStatus, PurchaseRecordStatusConstant.ARRIVED).
                list();
        //如果商品ID不为空的话，filter过滤一遍
        if (productId != null)
            purchaseRecords = purchaseRecords.stream().filter(p -> p.getProductId().equals(productId)).collect(Collectors.toList());

        //拿到了所有的购买记录，之后需要获取所有的退货记录
        List<Refund> refunds = refundService.lambdaQuery().
                eq(Refund::getSupplierId, supplierId).
                between(Refund::getRefundTime, startTime, endTime).
                eq(Refund::getStatus, InputOutputStatus.OUTPUT).
                list();
        //如果商品ID不为空的话，filter过滤一遍
        if (productId != null)
            refunds = refunds.stream().filter(r -> r.getProductId().equals(productId)).collect(Collectors.toList());

        //拿到了所有的purchasesRecord和refund，根据他们的商品ID汇总生成列表用来结算,返回的是列表信息，对于非此时间段进货的商品，若是此时间段退货了，不会被计算在内
        Map<Long, SettlementVo> map = new HashedMap<>();
        //先处理进货记录,若存在则更新，不存在则使用全参构造函数创建
        purchaseRecords.forEach(p -> {
            SettlementVo settlementVo = map.get(p.getProductId());
            if (settlementVo == null) {
                settlementVo = new SettlementVo(p.getProductId(), p.getSupplierId(), p.getProductName(),
                        p.getProductPrice(), 0, 0, 0, 0.0, null, null, new ArrayList<>(), new ArrayList<>());
            } else {
                //增加供货数量，给列表增加供货单ID
                settlementVo.setSupplyNum(settlementVo.getSupplyNum() + p.getNumber());
                settlementVo.getPurchaseRecordIdList().add(p.getId());
            }
            map.put(p.getProductId(), settlementVo);
        });
        //处理退货记录，退货记录对于之前不存在的需要创建并更新，对于之前存在的需要更新
        refunds.forEach(r -> {
            SettlementVo settlementVo = map.get(r.getProductId());
            if (settlementVo == null) {
                settlementVo = new SettlementVo(r.getProductId(), r.getSupplierId(), r.getProductName(),
                        r.getProductPrice(), 0, 0, 0, 0.0, null, null, new ArrayList<>(), new ArrayList<>());
            }
            //增加退货数量，给列表增加退货单ID
            settlementVo.setReturnNum(settlementVo.getReturnNum() + r.getRefundCount());
            settlementVo.getRefundRecordIdList().add(r.getId());
            map.put(r.getProductId(), settlementVo);
        });
        //计算结算数量和结算金额
        map.forEach((k, v) -> {
            v.setTotalNum(v.getSupplyNum() - v.getReturnNum());
            v.setTotalAmount(v.getTotalNum() * v.getPrice());
            //如果结算金额为负数说明需要供销商退款
            if (v.getTotalAmount() < 0) v.setSettleType(SettlementVo.REFUND);
            else v.setSettleType(SettlementVo.PAY);
            v.setTotalAmount(Math.abs(v.getTotalAmount()));
        });
        //如果不存在内容则返回错误
        if (map.size() == 0) return AjaxResult.error("此时间段内不存在相应条件的进货或退货记录");
        return AjaxResult.success(map.values());
    }


    @PostMapping("/feign/updateStatus")
    @ApiOperation("更新所有涉及的退货单或者购货单状态为已结算")
    AjaxResult updateStatus(@RequestBody List<SettlementVo> settlementVos) {
        //所有的都是已经请求结算的信息，我们找到对应的进货单以及退货单更新为已结算
        settlementVos.forEach(s -> {
            List<Long> purchaseRecordIdList = s.getPurchaseRecordIdList();
            purchaseRecordIdList.forEach(p -> purchaseRecordService.update(new UpdateWrapper<PurchaseRecord>().lambda().eq(PurchaseRecord::getId, p).set(PurchaseRecord::getStatus, PurchaseRecordStatusConstant.SETTLED)));
            List<Long> refundRecordIdList = s.getRefundRecordIdList();
            refundRecordIdList.forEach(r -> refundService.update(new UpdateWrapper<Refund>().lambda().eq(Refund::getId, r).set(Refund::getStatus, PurchaseRecordStatusConstant.SETTLED)));
        });
        return AjaxResult.success();
    }
}

