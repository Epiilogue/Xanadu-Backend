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
@TableName("ware_center_storage_record")
@ApiModel(value = "CenterStorageRecord对象", description = "")
public class CenterStorageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("可分配商品数量")
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

    @ApiModelProperty("已分配的商品数量")
    @TableField("allocated_num")
    private Integer allocatedNum;

    @ApiModelProperty("退货的商品数量")
    @TableField("refund_num")
    private Integer refundNum;

    @ApiModelProperty("下单锁定的商品数量")
    @TableField("lock_num")
    private Integer lockNum;


    @ApiModelProperty("总计商品数量")
    @TableField("total_num")
    private Integer totalNum;


    public CenterStorageRecord() {
        //所有的Integer类型都填0
        this.allocatedNum = 0;
        this.refundNum = 0;
        this.lockNum = 0;
        this.totalNum = 0;
    }
}
