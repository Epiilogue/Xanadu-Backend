package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 回执单
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Getter
@Setter
@TableName("sub_receipt")
@ApiModel(value = "Receipt对象", description = "回执单")
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回执录入日期")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("任务ID")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("客户姓名")
    @TableField("receiver_name")
    private String receiverName;

    @ApiModelProperty("客户联系电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("分站id")
    @TableField("sub_id")
    private Long subId;

    @ApiModelProperty("任务类型")
    @TableField("task_type")
    private String taskType;

    @ApiModelProperty("回执任务状态")
    @TableField("state")
    private String state;

    @ApiModelProperty("快递员ID")
    @TableField("courier_id")
    private Long courierId;


    @ApiModelProperty("客户满意度")
    @TableField("feedback")
    private Integer feedback;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("送货/退货地址")
    @TableField("delivery_address")
    private String deliveryAddress;

    @ApiModelProperty("签收时间")
    @TableField("sign_time")
    private Date signTime;

    @ApiModelProperty("回执录入日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("计划商品数量")
    @TableField("plan_num")
    private Integer planNum;

    @ApiModelProperty("计划金额")
    @TableField("plan_receipt")
    private Double planReceipt;

    @ApiModelProperty("签收数量")
    @TableField("sign_num")
    private Integer signNum;


    @ApiModelProperty("退回数量")
    @TableField("refund_num")
    private Integer refundNum;

    @ApiModelProperty("实际总收款")
    @TableField("input_money")
    private Double inputMoney;

    @ApiModelProperty("实际总退款")
    @TableField("output_money")
    private Double outputMoney;


    @ApiModelProperty("发票号")
    @TableField("invoice_number")
    private Long invoiceNumber;

    @ApiModelProperty("关联商品")
    @TableField(exist = false)
    private List<ReceiptProduct> receiptProducts;


    public Receipt() {
        //所有double,integer类型初始化为0,除了id
        this.planNum = 0;
        this.planReceipt = 0.0;
        this.signNum = 0;
        this.refundNum = 0;
        this.inputMoney = 0.0;
        this.outputMoney = 0.0;
    }
}
