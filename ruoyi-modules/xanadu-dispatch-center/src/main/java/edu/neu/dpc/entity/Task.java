package edu.neu.dpc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("dpc_task")
@ApiModel(value = "Task对象", description = "")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务单号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("分站id")
    @TableField("sub_id")
    private Long subId;


    @ApiModelProperty("任务单状态")
    @TableField("task_status")
    private String taskStatus;

    @ApiModelProperty("是否删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("任务类型")
    @TableField("task_type")
    private String taskType;


}
