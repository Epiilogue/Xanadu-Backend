package edu.neu.cc.vo;

import lombok.Data;

/**
 * 操作员、商品种类、商品名称、新订（笔数、数量、金额）、退货（笔数、数量、金额）、换货（笔数、数量）、退订（笔数、数量、金额）、净收入
 */
@Data
public class OperationContactResultVo {
    private Long userId;
    private Long productId;
    private String productName;
    private String productType;
    //新订
    private Integer newCount; //笔数
    private Integer newNumber;//数量
    private Double newAmount;//金额
    //退货
    private Integer returnCount; //笔数
    private Integer returnNumber;//数量
    private Double returnAmount;//金额
    //换货
    private Integer exchangeCount; //笔数
    private Integer exchangeNumber;//数量
    //退订
    private Integer cancelCount; //笔数
    private Integer cancelNumber;//数量
    private Double cancelAmount;//金额
}
