package edu.neu.dpc.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


//一对多，key为订单号、（商品ID，商品名称、商品数量、商品价格）
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dpc_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的记录ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("对应的订单ID号")
    private Long orderId;

    @ApiModelProperty("对应的任务ID号")
    private Long taskId;


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

    @ApiModelProperty("实际接收数量")
    @TableField("actual_number")
    private Integer actualNumber;


}