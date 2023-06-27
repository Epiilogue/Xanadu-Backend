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
 * @since 2023-06-26 10:34:41
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

    @ApiModelProperty("开始号码")
    @TableField("start_number")
    private String startNumber;

    @ApiModelProperty("结束号码")
    @TableField("end_number")
    private String endNumber;

    @ApiModelProperty("登记状态")
    @TableField("registration")
    private String registration;

    @ApiModelProperty("批次")
    @TableField("batch")
    private int batch;

    @ApiModelProperty("本数")
    @TableField("total")
    private int total;

    @ApiModelProperty("分站id")
    @TableField("substation_id")
    private String substationId;

    @ApiModelProperty("发票生成日期")
    @TableField("time")
    private Date time;


}
