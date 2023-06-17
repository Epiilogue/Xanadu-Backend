package edu.neu.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-06-04 05:08:31
 */
@Getter
@Setter
@TableName("ware_center_output")
@ApiModel(value = "CenterOutput对象", description = "")
public class CenterOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中心仓库出库记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("退货出库或调拨出库的ID")
    @TableField("output_id")
    private Long outputId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("出库数量")
    @TableField("output_num")
    private Integer outputNum;

    @ApiModelProperty("出库类型")
    @TableField("output_type")
    private String outputType;

    @ApiModelProperty("出库时间")
    @TableField("output_time")
    private Date outputTime;

    @ApiModelProperty("出库状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("供应商ID")
    @TableField("supplier_id")
    private Long supplierId;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("任务单ID")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("要求出库时间")
    @TableField("require_time")
    private Date requireTime;

    @ApiModelProperty("分库ID")
    @TableField("subware_id")
    private Long subwareId;

    @ApiModelProperty("商品单价")
    @TableField("product_price")
    private Double productPrice;

    @ApiModelProperty("操作人员ID")
    @TableField("operator_id")
    private Long operatorId;

    @ApiModelProperty("实际数量")
    @TableField("actual_num")
    private Integer actualNum;
}
