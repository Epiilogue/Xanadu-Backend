package edu.neu.dpc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
    //已提交，已完成
    public static final String SUBMITTED = "已提交";
    public static final String FINISHED = "已完成";


    private static final long serialVersionUID = 1L;

    @ApiModelProperty("调拨单号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("入库分库ID")
    @TableField("subware_id")
    private Long subwareId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品数量")
    @TableField("product_num")
    private Integer productNum;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("计划出库时间")
    @TableField("plan_time")
    private Date planTime;

    @ApiModelProperty("订单号")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("任务单号")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("调度状态")
    @TableField("status")
    private String status;

}
