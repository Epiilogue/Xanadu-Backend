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
 * 分站发票管理
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-26 10:34:41
 */
@Getter
@Setter
@TableName("ac_invoices")
@ApiModel(value = "Invoices对象", description = "分站发票管理")
public class Invoices implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("发票批次序号")
    @TableField("totalid")
    private Long totalid;

    @ApiModelProperty("发票号码")
    @TableField("number")
    private String number;

    @ApiModelProperty("发票领用状态")
    @TableField("state")
    private String state;

    @ApiModelProperty("批次")
    @TableField("batch")
    private int batch;

    @ApiModelProperty("分站id")
    @TableField("substation_id")
    private String substationId;

    @ApiModelProperty("领用人")
    @TableField("employee")
    private String employee;

    @ApiModelProperty("失效状态")
    @TableField("dstate")
    private String dstate;

    @ApiModelProperty("失效原因")
    @TableField("details")
    private String details;

    @ApiModelProperty("订单号")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty("打印时间")
    @TableField("printtime")
    private Date printTime;

    @ApiModelProperty("商品名称")
    @TableField("productName")
    private String productName;

    @ApiModelProperty("商品数量")
    @TableField("productNum")
    private String productNum;


}
