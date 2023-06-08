package edu.neu.dbc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2023-06-02 11:12:09
 */
@Getter
@Setter
@TableName("dbc_purchase_record")
@ApiModel(value = "PurchaseRecord对象", description = "")
public class PurchaseRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("进货单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("供销商ID")
    @TableField("supplier_id")
    private Long supplierId;

    @ApiModelProperty("进货数量")
    @TableField("number")
    private Integer number;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("进货状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("软删除标记")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


    @ApiModelProperty("商品单价")
    @TableField("product_price")
    private Double productPrice;


    @ApiModelProperty("采购单总消费")
    @TableField("total_cost")
    private Double totalCost;


    @ApiModelProperty("对应的缺货单的ID列表字符串")
    @TableField("lack_ids")
    private String lackIds;


}
