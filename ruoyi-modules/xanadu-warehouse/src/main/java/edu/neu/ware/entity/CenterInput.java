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
@TableName("ware_center_input")
@ApiModel(value = "CenterInput对象", description = "")
public class CenterInput implements Serializable {
    //状态已入库，未入库

    public static final String INPUT_STATUS_YES = "已入库";
    public static final String INPUT_STATUS_NO = "未入库";

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("入库记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("入库单号ID")
    @TableField("input_id")
    private Long inputId;

    @ApiModelProperty("入库类型，购货或是退单")
    @TableField("input_type")
    private String inputType;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("商品数量")
    @TableField("input_num")
    private Integer inputNum;

    @ApiModelProperty("商品价格")
    @TableField("product_price")
    private Double productPrice;

    @ApiModelProperty("入库日期")
    @TableField("input_time")
    private Date inputTime;


    @ApiModelProperty("入库状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("供应商ID")
    @TableField("supplier_id")
    private Long supplierId;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("分库ID")
    @TableField("subware_id")
    private Long subwareId;



}
