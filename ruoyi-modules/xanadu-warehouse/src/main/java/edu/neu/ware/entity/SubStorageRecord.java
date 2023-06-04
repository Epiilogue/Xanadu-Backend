package edu.neu.ware.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
@Getter
@Setter
@TableName("ware_sub_storage_record")
@ApiModel(value = "SubStorageRecord对象", description = "")
public class SubStorageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("子站ID")
    @TableField("subware_id")
    private Long subwareId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("可分配数量")
    @TableField("allocate_able_num")
    private Integer allocateAbleNum;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("商品价格")
    @TableField("product_price")
    private Double productPrice;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty("已分配数量")
    @TableField("allocated_num")
    private Integer allocatedNum;

    @ApiModelProperty("退货产品数量")
    @TableField("refund_num")
    private Integer refundNum;

    @ApiModelProperty("总计产品数量")
    @TableField("total_num")
    private Integer totalNum;


}
