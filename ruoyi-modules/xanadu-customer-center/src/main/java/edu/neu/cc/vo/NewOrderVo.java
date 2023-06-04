package edu.neu.cc.vo;


import com.baomidou.mybatisplus.annotation.*;
import edu.neu.cc.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 接受前端参数
 */
@Data
public class NewOrderVo  implements Serializable {
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

    @ApiModelProperty("订单状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("电话号码")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty("备注信息")
    @TableField("comment")
    private String comment;

    @ApiModelProperty("预计送货日期")
    @TableField("delivery_time")
    private Date deliveryTime;

    @ApiModelProperty("是否要发票")
    @TableField("invoice_need")
    private Integer invoiceNeed;

    @ApiModelProperty("送货地址")
    @TableField("delivery_address")
    private String deliveryAddress;

    @ApiModelProperty("接收人姓名")
    @TableField("receiver_name")
    private String receiverName;

    @ApiModelProperty("支付总价")
    @TableField("total_amount")
    private Double totalAmount;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("是否删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


    @ApiModelProperty("商品列表")
    @TableField(exist = false)
    private List<Product> products;


    @ApiModelProperty("涉及的商品数量")
    @TableField("numbers")
    private Integer numbers;



}
