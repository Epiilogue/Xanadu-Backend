package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@Getter
@Setter
@TableName("cc_product")
@ApiModel(value = "Product对象", description = "")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的记录ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("对应的订单ID号")
//    @TableId("order_id")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("数量")
    @TableField("number")
    private Integer number;

    @ApiModelProperty("商品单价")
    @TableField("price")
    private Double price;

    @ApiModelProperty("商品大类")
    @TableField("product_categary")
    private String productCategary;

    @ApiModelProperty("商品是否缺货")
    @TableField("islack")
    private Boolean islack;

    @ApiModelProperty("商品能否退货")
    @TableField("refund_able")
    private Boolean refundAble;

    @ApiModelProperty("商品能否换货")
    @TableField("change_able")
    private Boolean changeAble;


}
