package edu.neu.cc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2023-06-01 10:32:09
 */
@Getter
@Setter
@TableName("cc_new_order")
@ApiModel(value = "NewOrderDto对象", description = "")
public class NewOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的order主表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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


}
