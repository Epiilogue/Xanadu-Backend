package edu.neu.ware.entity;

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
 * @since 2023-06-15 02:25:09
 */
@Getter
@Setter
@TableName("ware_sub_input")
@ApiModel(value = "SubInput对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class SubInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("入库记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("调拨单ID，如果是退货则不需要该字段")
    @TableField("dispatch_id")
    private Long dispatchId;

    @ApiModelProperty("入库类型，购货或是退单")
    @TableField("input_type")
    private String inputType;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("预计商品数量")
    @TableField("input_num")
    private Integer inputNum;

    @ApiModelProperty("入库日期")
    @TableField("input_time")
    private Date inputTime;


    @ApiModelProperty("分库ID")
    @TableField("subware_id")
    private Long subwareId;

    @ApiModelProperty("商品价格")
    @TableField("product_price")
    private Double productPrice;

    @ApiModelProperty("供应商ID")
    @TableField("suplier_id")
    private Long suplierId;

    @ApiModelProperty("任务ID")
    @TableField("task_id")
    private Long taskId;

}
