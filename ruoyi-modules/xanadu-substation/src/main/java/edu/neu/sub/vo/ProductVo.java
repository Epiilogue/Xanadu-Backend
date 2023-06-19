package edu.neu.sub.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data

public class ProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的回执ID")
    private Long receiptId;


    @ApiModelProperty("对应的任务ID号")
    private Long taskId;


    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名")
    private String productName;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("商品单价")
    private Double price;

    @ApiModelProperty("商品大类")
    private String productCategary;

    @ApiModelProperty("实际接收数量")
    private Integer actualNumber;

    @ApiModelProperty("商品能否退货")
    private Boolean refundAble;

    @ApiModelProperty("失败原因")
    private String reason;

}