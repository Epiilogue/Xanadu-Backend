package edu.neu.cc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import edu.neu.cc.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RefundVo implements java.io.Serializable
{

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
    @TableField("total_amount")
    private Double totalAmount;

    @ApiModelProperty("涉及的商品数量")
    @TableField("numbers")
    private Integer numbers;

    @ApiModelProperty("订单状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("操作订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("原因")
    @TableField("reason")
    private String reason;

    @ApiModelProperty("操作类型(退货，换货)")
    @TableField("operation_type")
    private Integer operationType;

    @ApiModelProperty("需要换货的商品列表")
    List<Product> products;


}
