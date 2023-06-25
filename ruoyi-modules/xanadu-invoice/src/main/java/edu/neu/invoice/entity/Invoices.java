package edu.neu.invoice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 分站发票管理
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-08 09:58:46
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

    @ApiModelProperty("发票号码")
    @TableField("number")
    private String number;

    @ApiModelProperty("批次")
    @TableField("batch")
    private String batch;

    @ApiModelProperty("发票状态")
    @TableField("state")
    private String state;

    @ApiModelProperty("领用人姓名")
    @TableField("employee")
    private String employee;

    @ApiModelProperty("是否生效")
    @TableField("dstate")
    private String dstate;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private String substationId;

    @ApiModelProperty("失效原因")
    @TableField("details")
    private String details;

}
