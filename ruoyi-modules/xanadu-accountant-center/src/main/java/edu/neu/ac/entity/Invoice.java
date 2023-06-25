package edu.neu.ac.entity;

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
 * 发票记录
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-23 09:35:39
 */
@Getter
@Setter
@TableName("ac_invoice")
@ApiModel(value = "Invoice对象", description = "发票记录")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("发票号")
    @TableField("invoice_number")
    private String invoiceNumber;

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

    @ApiModelProperty("领用人姓名")
    @TableField("employee")
    private String employee;

    @ApiModelProperty("更新日期")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty("是否已删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
