package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 10:32:09
 */
@Getter
@Setter
@ApiModel(value = "NewOrder对象", description = "新订单")
public class NewOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的order主表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("订单状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("电话号码")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty("备注信息")
    @TableField("comment")
    private String comment;

    @ApiModelProperty("预计送货日期")
    @TableField("delivery_time")
    private Date deliveryTime;

    @ApiModelProperty("是否要发票")
    @TableField("invoice_need")
    private Integer invoiceNeed;

    @ApiModelProperty("送货地址")
    @TableField("delivery_address")
    private String deliveryAddress;

    @ApiModelProperty("接收人姓名")
    @TableField("receiver_name")
    private String receiverName;

    @ApiModelProperty("商品大类")
    @TableField("product_categray")
    private String productCategray;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private String productId;

    @ApiModelProperty("购买商品数量")
    @TableField("product_numbers")
    private Integer productNumbers;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("商品单价")
    @TableField("single_amount")
    private Double singleAmount;

    @ApiModelProperty("支付总价")
    @TableField("total_amount")
    private Double totalAmount;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;



}
