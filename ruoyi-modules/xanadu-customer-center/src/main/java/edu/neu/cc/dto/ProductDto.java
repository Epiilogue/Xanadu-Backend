package edu.neu.cc.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-06-01 10:32:09
 */
@Getter
@Setter
@TableName("cc_product")
@ApiModel(value = "ProductDto对象", description = "")
public class ProductDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的订单ID号")
    @TableId("order_id")
    private Long orderId;

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

    @ApiModelProperty("商品类别")
    @TableField("product_category")
    private String productCategory;

}
