package edu.neu.ware.vo;


import edu.neu.ware.entity.CenterOutput;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 商品编号、 商品名称、售价、 数量、供应商名称、备注、商品数量总计、 总金额、 日期
 */
@Data
@AllArgsConstructor
@Api("出库单")
public class InventoryVo {
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer number;
    private String supplierName;
    private String remark;
    private Integer totalNum;
    private Double totalPrice;
    private Date date;

    //子仓库名称
    private String subwareName;
    //操作员ID
    private Long operatorId;

    public InventoryVo(CenterOutput centerOutput) {
        this.productId = centerOutput.getProductId();
        this.productName = centerOutput.getProductName();
        this.productPrice = centerOutput.getProductPrice();
        this.number = 0;
        this.remark = "";
        this.totalPrice = 0.0;
        this.supplierName=null;
        this.totalNum=null;
    }


}
