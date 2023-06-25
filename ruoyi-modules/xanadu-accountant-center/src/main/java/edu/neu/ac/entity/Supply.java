package edu.neu.ac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

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
 * @since 2023-06-23 09:35:39
 */
@Getter
@Setter
@TableName("ac_supply")
@ApiModel(value = "Supply对象", description = "")
public class Supply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品ID")
    @TableId(value = "product_id")
    private Long productId;

    @ApiModelProperty("供应商ID")
    @TableField("supplier_id")
    private Long supplierId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("单价")
    @TableField("price")
    private Double price;

    @ApiModelProperty("供货数量")
    @TableField("supply_num")
    private Integer supplyNum;

    @ApiModelProperty("退回数量")
    @TableField("return_num")
    private Integer returnNum;

    @ApiModelProperty("结算数量")
    @TableField("total_num")
    private Integer totalNum;

    @ApiModelProperty("结算金额")
    @TableField("total_amount")
    private Double totalAmount;

    @ApiModelProperty("日期")
    @TableField("time")
    private Date time;

    @ApiModelProperty("删除标记")
    @TableField("deleted")
    private Boolean deleted;

    // settle_type
    @ApiModelProperty("退款还是支出")
    @TableField("settle_type")
    private String settleType;
}
