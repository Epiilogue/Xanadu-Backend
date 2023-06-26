package edu.neu.ac.entity;

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
 * @author jin Zhang
 * @since 2023-06-26 09:58:23
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

    @ApiModelProperty("总计金额")
    @TableField("amount")
    private Double amount;

    @ApiModelProperty("日期")
    @TableField("time")
    private Date time;

    @ApiModelProperty("是否结算")
    @TableField("deleted")
    @TableLogic
    private boolean deleted;

    @ApiModelProperty("退款还是支出")
    @TableField("settleType")
    private String settleType;


}
