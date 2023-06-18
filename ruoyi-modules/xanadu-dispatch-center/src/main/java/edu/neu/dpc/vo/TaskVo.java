package edu.neu.dpc.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.neu.dpc.entity.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class TaskVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务单ID")
    private Long id;

    @ApiModelProperty("客户ID")
    private Long customerId;

    @ApiModelProperty("接收人姓名")
    private String receiverName;

    @ApiModelProperty("接收人电话")
    private String phone;

    @ApiModelProperty("送货地址")
    private String deliveryAddress;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("分站id")
    private Long subId;


    @ApiModelProperty("商品总数")
    private Integer numbers;

    @ApiModelProperty("商品总价")
    private Double totalAmount;

    @ApiModelProperty("要求完成日期")
    private Date deadline;

    @ApiModelProperty("任务类型")
    private String taskType;

    @ApiModelProperty("任务状态")
    private String taskStatus;


    @ApiModelProperty("商品列表")
    private List<Product> products;


}
