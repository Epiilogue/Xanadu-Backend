package edu.neu.cc.vo;

import lombok.Data;

/**
 * 操作员、商品种类、商品名称、新订（笔数、数量、金额）、退货（笔数、数量、金额）、换货（笔数、数量）、退订（笔数、数量、金额）、净收入
 */
@Data
public class OperationResultVo implements java.io.Serializable {
    private Long productId;
    private String productName;
    private String productType;
    private Integer count; //笔数
    private Integer number;//数量
    private Double amount;//金额
   /* private String operationType;//操作类型*/
}
