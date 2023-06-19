package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 订单商品的签收情况
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Getter
@Setter
@TableName("sub_receipt_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ReceiptProduct对象", description = "订单商品的签收情况")
public class ReceiptProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回执ID")
    @TableField("receipt_id")
    private Long receiptId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("单价")
    @TableField("price")
    private Double price;

    @ApiModelProperty("总数量")
    @TableField("all_num")
    private Integer allNum;

    @ApiModelProperty("签收数量")
    @TableField("sign_num")
    private Integer signNum;

    @ApiModelProperty("退货数量")
    @TableField("return_num")
    private Integer returnNum;

    @ApiModelProperty("入账")
    @TableField("input_money")
    private Double inputMoney;

    @ApiModelProperty("出账")
    @TableField("output_money")
    private Double outputMoney;

}
