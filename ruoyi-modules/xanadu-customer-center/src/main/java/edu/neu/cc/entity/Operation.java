package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("cc_operation")
@ApiModel(value = "Operation对象", description = "")
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("操作客服ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("客户ID")
    @TableField("customer_id")
    private Long customerId;

    @ApiModelProperty("订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("操作类型")
    @TableField("operator_type")
    private String operatorType;

    @ApiModelProperty("操作商品数量")
    @TableField("numbers")
    private Integer numbers;

    @ApiModelProperty("操作金额")
    @TableField("total_amount")
    private Double totalAmount;


}
