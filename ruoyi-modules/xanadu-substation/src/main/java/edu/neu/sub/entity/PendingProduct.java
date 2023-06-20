package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-06-19 11:12:41
 */
@Getter
@Setter
@TableName("sub_pending_product")
@ApiModel(value = "PendingProduct对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PendingProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("任务ID")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("商品单价")
    @TableField("product_price")
    private Double productPrice;

    @ApiModelProperty("待处理数量")
    @TableField("deal_number")
    private Integer dealNumber;

    @ApiModelProperty("来源")
    @TableField("source")
    private String source;

    @ApiModelProperty("子库ID")
    @TableField("subware_id")
    private Long subwareId;


}
