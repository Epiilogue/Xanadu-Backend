package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 10:32:09
 */
@Getter
@Setter
@TableName("cc_product")
@ApiModel(value = "Product对象", description = "封装商品信息")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名")
    private String productName;

    @ApiModelProperty("商品单价")
    private Double price;

    @ApiModelProperty("商品大类")
    private String productCategory;

}
