package edu.neu.sub.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data

public class ProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的记录ID")
    @TableId("id")
    private Long id;


    @ApiModelProperty("对应的任务ID号")
    private Long taskId;


    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("数量")
    @TableField("number")
    private Integer number;

    @ApiModelProperty("商品单价")
    @TableField("price")
    private Double price;

    @ApiModelProperty("商品大类")
    @TableField("product_categary")
    private String productCategary;

    @ApiModelProperty("实际接收数量")
    @TableField("actual_number")
    private Integer actualNumber;


}