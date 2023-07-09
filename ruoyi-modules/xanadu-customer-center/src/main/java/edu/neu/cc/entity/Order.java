package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.*;

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
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@Getter
@Setter
@TableName("cc_order")
@ApiModel(value = "Order对象", description = "")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("操作人ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("客户ID")
    @TableField("customer_id")
    private Long customerId;

    @ApiModelProperty("创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("订单类型")
    @TableField("order_type")
    private String orderType;

    @ApiModelProperty("要求完成日期")
    @TableField("deadline")
    private Date deadline;

    @ApiModelProperty("涉及的金额")
    @TableField("total_amout")
    private Double totalAmount;


    @ApiModelProperty("涉及的商品数量")
    @TableField("numbers")
    private Integer numbers;

    @ApiModelProperty("订单状态")
    @TableField("status")
    private String status;


    //    deleted
    @ApiModelProperty("是否删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
