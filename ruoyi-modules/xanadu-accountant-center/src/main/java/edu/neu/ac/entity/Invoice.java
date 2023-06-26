package edu.neu.ac.entity;

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
 * 发票记录
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-26 09:58:23
 */
@Getter
@Setter
@TableName("ac_invoice")
@ApiModel(value = "Invoice对象", description = "发票记录")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private String substationId;

    @ApiModelProperty("发票号")
    @TableField("invoice_number")
    private String invoiceNumber;

    @ApiModelProperty("开始号码")
    @TableField("start_number")
    private String startNumber;

    @ApiModelProperty("结束号码")
    @TableField("end_number")
    private String endNumber;

    @ApiModelProperty("登记状态")
    @TableField("registration")
    private String registration;

    @ApiModelProperty("发票状态")
    @TableField("state")
    private String state;

    @ApiModelProperty("批次")
    @TableField("batch")
    private String batch;

    @ApiModelProperty("订单号")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("金额")
    @TableField("amount")
    private Double amount;

    @ApiModelProperty("使用人姓名")
    @TableField("user")
    private String user;

    @ApiModelProperty("本数")
    @TableField("total")
    private String total;

    @ApiModelProperty("领用人姓名")
    @TableField("employee")
    private String employee;

    @ApiModelProperty("发票生成日期")
    @TableField("time")
    private Date time;

    @ApiModelProperty("发票是否生效")
    @TableField("dstate")
    private String dstate;

    @ApiModelProperty("备注")
    @TableField("details")
    private String details;


}
