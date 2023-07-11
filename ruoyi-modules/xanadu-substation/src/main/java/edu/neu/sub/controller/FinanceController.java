package edu.neu.sub.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.Finance;
import edu.neu.sub.entity.Receipt;
import edu.neu.sub.entity.ReceiptProduct;
import edu.neu.sub.entity.Substation;
import edu.neu.sub.feign.CategaryClient;
import edu.neu.sub.service.ReceiptProductService;
import edu.neu.sub.service.ReceiptService;
import edu.neu.sub.service.SubstationService;
import org.aspectj.lang.annotation.DeclareWarning;
import org.bouncycastle.cms.Recipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 商品收款 前端控制器
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@RestController
@RequestMapping("/sub/finance")
@Transactional(rollbackFor = Exception.class)
public class FinanceController {
    /**
     * 录入时间段、商品种类查询条件，查询本分站一段时间内每种商品的送货收款情况，以及总的收款退款金额。
     * 商品种类、 商品名称、送货商品数量、 收款额、 退货商品数量、实收额、退款额、应缴额
     * 所用的送货数量 所有的收款总金额
     * <p>
     * 解决方案：要求用户输入大类以及小类以及时间段，用户输入大类得到小类并最终得到一个商品小类ID
     * 1.拿到分类ID，根据ID拿到所有的商品ID
     * 2.拿到当前的分站ID
     * 3.根据时间段利用回执统计商品信息
     */

    @Autowired
    CategaryClient categaryClient;

    @Autowired
    SubstationService substationService;

    @Autowired
    ReceiptService receiptService;

    @Autowired
    ReceiptProductService receiptProductService;

    /**
     * 前端需要传回分站ID，商品种类ID，商品种类名称，时间段,支持远程调用
     */
    @GetMapping("/getPaymentByCategoryAndTime/{substationId}")
    public AjaxResult getPaymentByCategoryAndTime(@PathVariable("substationId") Long substationId, @RequestParam("categaryId") Integer categaryId,
                                                  @RequestParam("startTime") String startTime,
                                                  @RequestParam("endTime") String endTime,
                                                  @RequestParam(value = "productName", required = false) String productName) {


        if (startTime == null || endTime == null) return AjaxResult.error("时间段不能为空");
        AjaxResult goodIdsRes = categaryClient.getGoods(categaryId);
        if (goodIdsRes == null) return AjaxResult.error("获取商品ID列表失败");
        if (goodIdsRes.isError()) return AjaxResult.error(goodIdsRes.getMsg());
        //忽略警告
        @SuppressWarnings("unchecked")
        List<Long> goodIds = JSON.parseArray(JSON.toJSONString(goodIdsRes.get("data")),Long.class);
        if (goodIds == null || goodIds.size() == 0) return AjaxResult.error("该分类下没有商品");
        Substation substation = substationService.getById(substationId);
        if (substation == null) return AjaxResult.error("分站不存在");
        //筛选出分站对应时间段内的所有的收据，我们按照创建时间来执行，签收时间由操作人录入容易出现误差
        QueryWrapper<Receipt> recipientQueryWrapper = new QueryWrapper<Receipt>().eq("sub_id", substationId).between("create_time", startTime, endTime);
        List<Receipt> receiptList = receiptService.list(recipientQueryWrapper);
        if (receiptList == null || receiptList.size() == 0) return AjaxResult.error("该时间段内没有收据");
        //根据收据拿到所有关联的商品信息
        for (Receipt receipt : receiptList) {
            if (receipt.getTaskType().equals(TaskType.PAYMENT)) continue;
            QueryWrapper<ReceiptProduct> receiptProductQueryWrapper = new QueryWrapper<ReceiptProduct>().eq("receipt_id", receipt.getId());
            List<ReceiptProduct> receiptProductList = receiptProductService.list(receiptProductQueryWrapper);
            receiptProductList = receiptProductList.stream().filter(receiptProduct -> goodIds.contains(receiptProduct.getProductId())).collect(Collectors.toList());
            if (productName != null)
                receiptProductList = receiptProductList.stream().filter(receiptProduct -> receiptProduct.getProductName().contains(productName)).collect(Collectors.toList());
            //将过滤后的商品信息放入收据中
            receipt.setReceiptProducts(receiptProductList);
        }
        //接下来开始在内存内统计数据，先创建map存放所有的商品记录
        Map<Long, Finance> productFinanceMap = new HashMap<>();
        receiptList.forEach(r -> {
            r.getReceiptProducts().forEach(p -> {
                Long productId = p.getProductId();
                if (!productFinanceMap.containsKey(productId)) {
                    Finance finance = new Finance(r.getSubId(), p.getProductName(), 0, 0.0, 0, 0, 0.0, 0.0, 0.0);
                    productFinanceMap.put(productId, finance);
                }
                Finance finance = productFinanceMap.get(productId);
                //拿到保存的数据，接下来需要根据当前的记录类型与商品记录的数据进行对应的更新
                finance.update(r, p);
                System.out.println(finance);
            });
            //不用管收款的事情，收款由送货数据负责更新完成
        });
        //更新，待缴额为实收额*0.9
        productFinanceMap.values().forEach(finance -> finance.setPay(finance.getActual() * 0.9));
        return AjaxResult.success(productFinanceMap.values());
    }


    @GetMapping("/feign/getPaymentByCategoryAndTime/{substationId}")
    public AjaxResult getPaymentFromRemote(@PathVariable("substationId") Long substationId, @RequestParam("categaryId") Integer categaryId, @RequestParam("categaryName") String categaryName,
                                           @RequestParam("startTime") String startTime,
                                           @RequestParam("endTime") String endTime,
                                           @RequestParam(value = "productName", required = false) String productName) {
        return this.getPaymentByCategoryAndTime(substationId, categaryId, startTime, endTime, productName);
    }
}

