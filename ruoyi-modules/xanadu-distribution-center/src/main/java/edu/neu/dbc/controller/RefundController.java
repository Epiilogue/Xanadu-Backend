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
import edu.neu.dbc.vo.StorageVo;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import static com.ruoyi.common.core.utils.PageUtils.startPage;

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
    @ApiOperation("查询所有历史退货安排")
    public AjaxResult list() {
        return AjaxResult.success(refundService.list());
    }

    @GetMapping("/feign/getRefundId/{refundId}")
    AjaxResult updateRefundStatus(@PathVariable("refundId") Long refundId) {
        Refund refund = refundService.getById(refundId);
        if (refund == null) return AjaxResult.error("退货安排不存在");
        refund.setStatus(InputOutputStatus.OUTPUT);
        refundService.updateById(refund);
        return AjaxResult.success();
    }

    @GetMapping("/searchForReturn")
    @ApiOperation("根据供应商 、进货日期段 、商品号查询需要进行退货安排的商品 ，查询结果包含以下信息： 查询采购单，采购单为已采购、已到货的都算入进货数量，以及查询当前的商品库存数")
    public AjaxResult listForReturn(@ApiParam("供应商ID") @RequestParam(value = "supplierId",required = false) Long supplierId,
                                    @ApiParam("商品ID") @RequestParam(value = "productId",required = false) Long productId,
                                    @ApiParam("开始时间") @RequestParam("startTime") Date startTime,
                                    @ApiParam("结束时间") @RequestParam("endTime") Date endTime) {
        if (startTime == null || endTime == null) {
            return AjaxResult.error("时间范围为空");
        }
        if (startTime.after(endTime)) {
            return AjaxResult.error("开始时间不能大于结束时间");
        }
        if (supplierId == null && productId == null) {
            return AjaxResult.error("供应商ID与商品ID都为空");
        }

        Product product;
        Supplier supplier;
        QueryWrapper<PurchaseRecord> queryWrapper;
        List<PurchaseRecord> list;

        if (supplierId == null) {
            product = productService.getById(productId);
            if (product == null) return AjaxResult.success("商品不存在");
            //1.查询采购单，采购单为已采购、已到货的都算入进货数量，需要注意时间范围，商品ID，供货商ID
            queryWrapper = new QueryWrapper<PurchaseRecord>().
                    eq("product_id", productId).between("create_time", startTime, endTime).
                    or(i -> i.eq("status", PurchaseRecordStatusConstant.PURCHASED).or(q->q.eq("status", PurchaseRecordStatusConstant.ARRIVED)));
        } else {
            supplier = supplierService.getById(supplierId);
            if (supplier == null) return AjaxResult.success("供应商不存在");
            if (productId == null)
                //1.查询采购单，采购单为已采购、已到货的都算入进货数量，需要注意时间范围，商品ID，供货商ID
                queryWrapper = new QueryWrapper<PurchaseRecord>().
                        eq("supplier_id", supplierId).between("create_time", startTime, endTime).
                        or(i -> i.eq("status", PurchaseRecordStatusConstant.PURCHASED).eq("status", PurchaseRecordStatusConstant.ARRIVED));
            else
                //1.查询采购单，采购单为已采购、已到货的都算入进货数量，需要注意时间范围，商品ID，供货商ID
                queryWrapper = new QueryWrapper<PurchaseRecord>().
                        eq("supplier_id", supplierId).between("create_time", startTime, endTime).eq("product_id", productId).
                        or(i -> i.eq("status", PurchaseRecordStatusConstant.PURCHASED).eq("status", PurchaseRecordStatusConstant.ARRIVED));


        }
        //对于每一个商品，我们都要进行映射，映射未对应的退货记录
        //查询采购单，采购单为已采购、已到货的都算入进货数量，以及查询当前的商品库存数
        //获取到所有的记录
        list = purchaseRecordService.list(queryWrapper);
        if (list == null || list.size() == 0) return AjaxResult.success("查询结果为空");
        Map<Long, Refund> refundMap = new HashMap<>();
        list.forEach(p -> {
            //对于每一条购买记录，我们都需要将其映射未对应的商品refund记录列表
            Long searchId = p.getProductId();
            if (!refundMap.containsKey(searchId)) {
                StorageVo storageVo = wareCenterStorageRecordClient.getStorage(productId);
                if (storageVo == null) return;
                Refund refund = new Refund(null, p.getSupplierId(), p.getProductId(), p.getProductName(), p.getProductPrice(), 0,
                        storageVo.getTotalNum(), storageVo.getReturnedNum(), null, false, null);
                refundMap.put(searchId, refund);
            }
            //取出对应的退货记录
            Refund refund = refundMap.get(searchId);
            refund.setInputNum(refund.getInputNum() + p.getNumber());
        });
        List<Refund> collect = new ArrayList<>(refundMap.values());
        return AjaxResult.success(collect);
    }


    @PostMapping("/generateReturnOrder/{number}")
    @ApiOperation("生成中心仓库退货单")
    public AjaxResult generateReturnOrder(@ApiParam("退货Vo") @RequestBody Refund refund, @PathVariable("number") Integer number) {
        //number为退货数量，用户前端输入，表示要退货的商品数量

        if (refund == null) return AjaxResult.error("退货信息为空");
        if (refund.getRefundCount() < number)
            return AjaxResult.error("退货数量不合法");
        //更新原记录，修改退货状态
        refund.setRefundCount(number);
        refund.setRefundTime(new Date());
        refund.setStatus(InputOutputStatus.SUBMITTED);
        boolean b = refundService.save(refund);
        if (!b) throw new ServiceException("退货记录保存失败");

        //生成退货单,远程调用写入退货单，
        CenterOutputVo returnRecord = new CenterOutputVo(refund.getId(), refund.getProductId(), refund.getProductName(), refund.getRefundCount()
                , InputOutputType.RETURN_OUT, new Date(), InputOutputStatus.NOT_OUTPUT, refund.getSupplierId(), refund.getProductPrice());
        if (!centerWareClient.addOutputRecord(returnRecord)) throw new ServiceException("退货单提交失败");
        return AjaxResult.success("退货单提交成功");

    }

}

