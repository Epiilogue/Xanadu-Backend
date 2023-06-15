package edu.neu.dbc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.InputOutputStatus;
import edu.neu.base.constant.cc.InputOutputType;
import edu.neu.base.constant.cc.PurchaseRecordStatusConstant;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.entity.PurchaseRecord;
import edu.neu.dbc.entity.Refund;
import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.feign.CenterWareClient;
import edu.neu.dbc.feign.OrderClient;
import edu.neu.dbc.feign.WareCenterStorageRecordClient;
import edu.neu.dbc.service.ProductService;
import edu.neu.dbc.service.PurchaseRecordService;
import edu.neu.dbc.service.RefundService;
import edu.neu.dbc.service.SupplierService;
import edu.neu.dbc.vo.CenterOutputVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@RestController
@RequestMapping("/dbc/refund")
public class RefundController {


    @Autowired
    PurchaseRecordService purchaseRecordService;

    @Autowired
    SupplierService supplierService;

    @Autowired
    @ApiModelProperty("远程订单服务")
    OrderClient orderClient;

    @Autowired
    CenterWareClient centerWareClient;


    @Autowired
    private ProductService productService;


    @Autowired
    private WareCenterStorageRecordClient wareCenterStorageRecordClient;

    @Autowired
    private RefundService refundService;


    @GetMapping("/list")
    @ApiOperation("查询所有退货安排")
    public AjaxResult list() {
        return AjaxResult.success(refundService.list());
    }

    @GetMapping("/searchForReturn")
    @ApiOperation("根据供应商 、进货日期段 、商品号查询需要进行退货安排的商品 ，查询结果包含以下信息： 查询采购单，采购单为已采购、已到货的都算入进货数量，以及查询当前的商品库存数")
    public AjaxResult listForReturn(@ApiParam("供应商ID") @RequestParam("supplierId") Long supplierId,
                                    @ApiParam("商品ID") @RequestParam("productId") Long productId,
                                    @ApiParam("开始时间") @RequestParam("startTime") Date startTime,
                                    @ApiParam("结束时间") @RequestParam("endTime") Date endTime) {
        if (supplierId == null) return AjaxResult.error("供应商ID为空");
        Supplier supplier = supplierService.getById(supplierId);
        if (supplier == null) return AjaxResult.error("供应商不存在");

        if (productId == null) return AjaxResult.error("商品ID为空");
        Product product = productService.getById(productId);
        if (product == null) return AjaxResult.error("商品不存在");
        if (startTime == null || endTime == null) return AjaxResult.error("时间范围为空");
        if (startTime.after(endTime)) return AjaxResult.error("开始时间不能大于结束时间");
        //查询采购单，采购单为已采购、已到货的都算入进货数量，以及查询当前的商品库存数

        //1.查询采购单，采购单为已采购、已到货的都算入进货数量，需要注意时间范围，商品ID，供货商ID
        QueryWrapper<PurchaseRecord> queryWrapper = new QueryWrapper<PurchaseRecord>().
                eq("supplier_id", supplierId).eq("product_id", productId).between("create_time", startTime, endTime).
                or(i -> i.eq("status", PurchaseRecordStatusConstant.PURCHASED).eq("status", PurchaseRecordStatusConstant.ARRIVED));
        //获取到所有的记录
        List<PurchaseRecord> list = purchaseRecordService.list(queryWrapper);
        //统计所有的进货数量
        Integer inputCount = list.stream().map(PurchaseRecord::getNumber).reduce(0, Integer::sum);
        //2.查询当前的商品库存数
        Integer storage = wareCenterStorageRecordClient.getStorage(productId).getTotalNum();
        //生成退货记录
        Refund refund = new Refund(null, supplierId, productId, product.getName(), product.getPrice(), inputCount, storage, 0, InputOutputStatus.NOT_SUBMIT, false,null);
        //保存至数据库
        boolean saved = refundService.save(refund);

        if (!saved) throw new ServiceException("退货单生成失败");

        return AjaxResult.success(refund);
    }


    @PostMapping("/generateReturnOrder")
    @ApiOperation("生成中心仓库退货单")
    public AjaxResult generateReturnOrder(@ApiParam("退货Vo") @RequestBody Refund refund) {
        if (refund == null) return AjaxResult.error("退货信息为空");
        if (refund.getRefundCount() > refund.getNowCount())
            return AjaxResult.error("退货数量不能大于库存数量");
        //更新原记录，修改退货状态
        refund.setStatus(InputOutputStatus.SUBMITTED);
        boolean b = refundService.updateById(refund);
        if (!b) throw new ServiceException("退货记录更新失败");

        //生成退货单,远程调用写入退货单，
        CenterOutputVo returnRecord = new CenterOutputVo(refund.getId(), refund.getProductId(), refund.getProductName(), refund.getRefundCount()
                , InputOutputType.RETURN_OUT, new Date(), InputOutputStatus.NOT_OUTPUT, refund.getSupplierId(), refund.getProductPrice());
        if (!centerWareClient.addOutputRecord(returnRecord)) throw new ServiceException("退货单提交失败");
        return AjaxResult.success("退货单提交成功");

    }

}

