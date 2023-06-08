package edu.neu.dbc.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 入库调拨单的vo对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenterInputVo implements Serializable {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty("入库记录ID")
    private Long id;

    @ApiModelProperty("入库单号ID")
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

    @ApiModelProperty("来源ID")
    private Long sourceId;



}
