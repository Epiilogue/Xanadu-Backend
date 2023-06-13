package edu.neu.dpc.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
@Getter
@Setter
@TableName("dpc_dispatch")
@ApiModel(value = "Dispatch对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class Dispatch implements Serializable {

    public static final String NOT_OUTPUT = "未出库";
    public static final String OUTPUTED = "已出库";


    private static final long serialVersionUID = 1L;

    @ApiModelProperty("调拨单号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("入库分库ID")
    @TableField("subware_id")
    private Long subwareId;

    @ApiModelProperty("任务号，该字段只有自动调度的才有值，手动调度的为null")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品数量")
    @TableField("product_num")
    private Integer productNum;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("商品大类")
    @TableField("product_categary")
    private String productCategary;

    @ApiModelProperty("计划出库时间")
    @TableField("plan_time")
    private Date planTime;

    @ApiModelProperty("调度状态")
    @TableField("status")
    private String status;


    @ApiModelProperty("软删除标记")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

}
