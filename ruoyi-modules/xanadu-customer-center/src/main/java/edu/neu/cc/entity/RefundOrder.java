package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@ApiModel(value = "RefundOrder对象", description = "退货换货订单实体类")
public class RefundOrder extends Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("操作订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("操作数量")
    @TableField("numbers")
    private Integer numbers;

    @ApiModelProperty("原因")
    @TableField("reason")
    private String reason;

    @ApiModelProperty("操作类型(退货，换货)")
    @TableField("operation_type")
    private Integer operationType;

    @ApiModelProperty("退换货状态")
    @TableField("status")
    private String status;


}
