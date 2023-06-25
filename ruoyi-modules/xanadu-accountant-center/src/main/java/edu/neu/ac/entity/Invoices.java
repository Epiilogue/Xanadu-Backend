package edu.neu.ac.entity;

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
 * @since 2023-06-23 09:35:39
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

    @ApiModelProperty("开始号码")
    @TableField("start")
    private String start;

    @ApiModelProperty("结束号码")
    @TableField("end")
    private String end;

    @ApiModelProperty("批次")
    @TableField("batch")
    private String batch;

    @ApiModelProperty("可用数量")
    @TableField("available")
    private Integer available;


}