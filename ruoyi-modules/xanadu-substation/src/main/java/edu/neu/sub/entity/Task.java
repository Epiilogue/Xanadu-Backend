package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import edu.neu.sub.vo.ProductVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("客户ID")
    @TableField("customer_id")
    private Long customerId;

    @ApiModelProperty("接收人姓名")
    @TableField("receiver_name")
    private String receiverName;

    @ApiModelProperty("接收人电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("送货地址")
    @TableField("delivery_address")
    private String deliveryAddress;

    @ApiModelProperty("订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("快递员ID")
    @TableField("courier_id")
    private Long courierId;

    @ApiModelProperty("商品总数")
    @TableField("numbers")
    private Integer numbers;

    @ApiModelProperty("商品总价")
    @TableField("total_amount")
    private Double totalAmount;

    @ApiModelProperty("要求完成日期")
    @TableField("deadline")
    private Date deadline;

    @ApiModelProperty("任务生成日期")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty("任务类型")
    @TableField("task_type")
    private String taskType;

    @ApiModelProperty("任务状态")
    @TableField("task_status")
    private String taskStatus;

    //    products_json
    @ApiModelProperty("商品信息列表json")
    @TableField("products_json")
    private String productsJson;

    @ApiModelProperty("分站id")
    @TableField("sub_id")
    private Long subId;


    @ApiModelProperty("商品列表")
    @TableField(exist = false)
    private List<ProductVo> products;

    @ApiModelProperty("签收单ID")
    @TableField("receipt_id")
    private Long receiptId;

    @ApiModelProperty("删除标记")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    public Task() {
        deleted = false;
    }
}
