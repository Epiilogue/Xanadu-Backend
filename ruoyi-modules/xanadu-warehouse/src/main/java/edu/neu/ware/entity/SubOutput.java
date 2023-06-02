package edu.neu.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-06-02 03:42:21
 */
@Getter
@Setter
@TableName("ware_sub_output")
@ApiModel(value = "SubOutput对象", description = "")
public class SubOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中心仓库出库记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("退货出库或调拨出库的ID")
    @TableField("output_id")
    private Long outputId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("出库数量")
    @TableField("ouput_num")
    private Integer ouputNum;

    @ApiModelProperty("出库类型(退货或者领货)")
    @TableField("output_type")
    private String outputType;

    @ApiModelProperty("出库时间")
    @TableField("output_time")
    private Date outputTime;

    @ApiModelProperty("出库分站ID")
    @TableField("subware_id")
    private Long subwareId;


}
