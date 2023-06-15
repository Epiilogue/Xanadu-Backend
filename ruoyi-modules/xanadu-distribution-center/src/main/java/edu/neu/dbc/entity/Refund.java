package edu.neu.dbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@Getter
@Setter
@TableName("dbc_refund")
@ApiModel(value = "Refund对象", description = "")
@AllArgsConstructor
public class Refund implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退货安排ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("供应商ID")
    @TableField("supplier_id")
    private Long supplierId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("商品单价")
    @TableField("product_price")
    private Double productPrice;


    @ApiModelProperty("进货数量")
    @TableField("input_num")
    private Integer inputNum;

    @ApiModelProperty("现库存量")
    @TableField("now_count")
    private Integer nowCount;

    @ApiModelProperty("退货数量")
    @TableField("refund_count")
    private Integer refundCount;

    @ApiModelProperty("退货状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("是否删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("退货时间")
    @TableField("refund_time")
    private Date refundTime;


}
