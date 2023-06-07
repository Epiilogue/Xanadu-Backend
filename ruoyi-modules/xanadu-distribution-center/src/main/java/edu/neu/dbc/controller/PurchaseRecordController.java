package edu.neu.dbc.controller;


import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.PurchaseRecordStatusConstant;
import edu.neu.base.constant.cc.StockoutConstant;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.entity.PurchaseRecord;
import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.feign.OrderClient;
import edu.neu.dbc.service.PurchaseRecordService;
import edu.neu.dbc.service.SupplierService;
import edu.neu.dbc.vo.AllLackRecordVo;
import edu.neu.dbc.vo.SingleLackRecordVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

        //完成校验后，生成采购单
        PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setNumber(allLackRecordVo.getInputCount());
        purchaseRecord.setProductId(product.getId());
        purchaseRecord.setProductName(product.getName());
        purchaseRecord.setProductPrice(product.getPrice());
        purchaseRecord.setSupplierId(supplierId);
        purchaseRecord.setNumber(allLackRecordVo.getInputCount());

        //设置采购单状态
        purchaseRecord.setStatus(PurchaseRecordStatusConstant.PURCHASED);
        purchaseRecord.setTotalCost(allLackRecordVo.getInputCount() * product.getPrice());
        //插入数据库
        boolean saved = purchaseRecordService.save(purchaseRecord);
        if (!saved) throw new ServiceException("采购单生成失败");
        //接下来需要找到所有的相关采购记录，将其状态置为已采购，需要调用远程服务
        List<Long> orderIdList = allLackRecordVo.getSingleLackRecordVos().stream().map(SingleLackRecordVo::getOrderId).collect(Collectors.toList());
        boolean updated = orderClient.batchUpdateStatus(StockoutConstant.PURCHASED, orderIdList);
        if (!updated) throw new ServiceException("采购单生成失败");

        return AjaxResult.success("采购单生成成功");
    }


}

