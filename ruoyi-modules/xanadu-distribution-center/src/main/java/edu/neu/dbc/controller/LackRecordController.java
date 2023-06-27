package edu.neu.dbc.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.dbc.vo.AllLackRecordVo;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.feign.StockoutClient;
import edu.neu.dbc.feign.WareCenterStorageRecordClient;
import edu.neu.dbc.service.ProductService;
import edu.neu.dbc.vo.SingleLackRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@RestController
@RequestMapping("/dbc/lackRecord")
@Api(tags = "LackRecordController", description = "提供缺货检查相关的 Rest API")
public class LackRecordController {


    @Autowired
    StockoutClient stockoutClient;
    @Autowired
    ProductService productService;
    @Autowired
    WareCenterStorageRecordClient wareCenterStorageRecordClient;

    public AllLackRecordVo getLackRecordById(Product product, Boolean isCheck) {
        List<SingleLackRecordVo> remoteLackRecords = stockoutClient.getLackRecordById(product.getId());
        //查询库存量
        Integer storage = wareCenterStorageRecordClient.getStorage(product.getId()).getTotalNum();
        assert storage != null;
        if (isCheck && storage < product.getSafeStock()) {
            Integer lackNumber = product.getSafeStock() - storage;
            SingleLackRecordVo checkSingleLackRecordVo = new SingleLackRecordVo();
            checkSingleLackRecordVo.setId(-1L);
            checkSingleLackRecordVo.setSource("缺货检查");
            checkSingleLackRecordVo.setCreateTime(new Date());
            checkSingleLackRecordVo.setNeedNumbers(lackNumber);
            remoteLackRecords.add(checkSingleLackRecordVo);
        }
        if (remoteLackRecords.size() == 0) return null;
        //需要给出当前库存数量，缺货数量
        AllLackRecordVo allLackRecordVo = new AllLackRecordVo();
        allLackRecordVo.setSingleLackRecordVos(remoteLackRecords);
        allLackRecordVo.setProduct(product);
        allLackRecordVo.setNowCount(storage);
        allLackRecordVo.setCreateTime(new Date());
        //计算缺货数量，然后生成进货数量
        Integer needCount = remoteLackRecords.stream().
                map(SingleLackRecordVo::getNeedNumbers).
                reduce(Integer::sum).orElse(0);
        allLackRecordVo.setNeedCount(needCount);
        allLackRecordVo.setInputCount(needCount);
        return allLackRecordVo;
    }

    @GetMapping("/getLackRecord/{productId}/{isCheck}")
    @ApiOperation("获取缺货记录,需要指定是否需要检查安全库存")
    public AjaxResult getLackRecord(
            @ApiParam("商品ID") @PathVariable("productId") Long productId,
            @ApiParam(value = "是否需要检查安全库存", defaultValue = "true") @PathVariable("isCheck") Boolean isCheck) {
        if (productId == null) return AjaxResult.error("商品ID不能为空");
        Product product = productService.getById(productId);
        if (product == null) return AjaxResult.error("商品不存在");
        AllLackRecordVo allLackRecordVo = getLackRecordById(product, isCheck);
        if (allLackRecordVo == null) return AjaxResult.error("没有缺货记录");
        return AjaxResult.success(allLackRecordVo);
    }

    @GetMapping("/getAllLackRecord")
    @ApiOperation("获取所有缺货记录")
    public AjaxResult getAllLackRecord() {
        List<Product> products = productService.list();
        if (products == null || products.size() == 0) return AjaxResult.error("没有商品");
        List<AllLackRecordVo> allLackRecordVos = products.stream().
                map(product -> getLackRecordById(product, true)).
                filter(Objects::nonNull).
                collect(java.util.stream.Collectors.toList());
        if (allLackRecordVos.size() == 0) return AjaxResult.error("没有缺货记录");
        return AjaxResult.success(allLackRecordVos);
    }
}