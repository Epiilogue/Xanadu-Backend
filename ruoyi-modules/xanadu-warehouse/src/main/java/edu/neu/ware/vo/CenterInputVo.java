package edu.neu.ware.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 入库调拨单的vo对象
 */
@Data
public class CenterInputVo implements Serializable {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty("入库记录ID")
    private Long id;

    @ApiModelProperty("入库单号ID,可能为购货单或者是退货单")
    private Long inputId;

    @ApiModelProperty("入库类型，购货或是退单")
    private String inputType;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品数量")
    private Integer inputNum;

    @ApiModelProperty("商品价格")
    private Double productPrice;

    @ApiModelProperty("入库日期")
    private Date inputTime;


    @ApiModelProperty("入库状态")
    private String status;

    @ApiModelProperty("供应商ID")
    private Long supplierId;

    @ApiModelProperty("分站ID")
    private Long substationId;

    @ApiModelProperty("分库ID")
    private Long subwareId;



}
