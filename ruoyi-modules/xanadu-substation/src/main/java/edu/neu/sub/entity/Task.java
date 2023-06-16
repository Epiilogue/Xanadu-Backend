package edu.neu.sub.entity;

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
 * 任务单
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 04:10:48
 */
@Getter
@Setter
@TableName("sub_task")
@ApiModel(value = "Task对象", description = "任务单")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("客户ID")
    @TableField("customer_id")
    private Long customerId;

    @ApiModelProperty("客户姓名")
    @TableField("customer_name")
    private String customerName;

    @ApiModelProperty("订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("快递员ID")
    @TableField("courier_id")
    private Long courierId;

    @ApiModelProperty("商品总数")
    @TableField("product_numbers")
    private Integer productNumbers;

    @ApiModelProperty("商品总价")
    @TableField("total_amount")
    private Double totalAmount;

    @ApiModelProperty("要求完成日期")
    @TableField("finish_need")
    private Date finishNeed;

    @ApiModelProperty("实际完成日期")
    @TableField("finish_real")
    private Date finishReal;

    @ApiModelProperty("任务类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("任务状态")
    @TableField("state")
    private Integer state;

    @ApiModelProperty("是否被删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
