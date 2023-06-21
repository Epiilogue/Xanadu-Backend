package edu.neu.ware.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@TableName("ware_sub_output")
@ApiModel(value = "SubOutput对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class SubOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分仓库出库记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("退货出库或任务ID")
    @TableField("output_id")
    private Long outputId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("出库数量")
    @TableField("output_num")
    private Integer outputNum;

    @ApiModelProperty("出库类型(退货或者领货)")
    @TableField("output_type")
    private String outputType;

    @ApiModelProperty("出库时间")
    @TableField("output_time")
    private Date outputTime;

    @ApiModelProperty("出库分站ID")
    @TableField("subware_id")
    private Long subwareId;

    @ApiModelProperty("软删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("出库状态,未出库，已出库")
    @TableField("status")
    private String status;


    @ApiModelProperty("实际的出库数量")
    @TableField("actual_num")
    private Integer actualNum;

}
