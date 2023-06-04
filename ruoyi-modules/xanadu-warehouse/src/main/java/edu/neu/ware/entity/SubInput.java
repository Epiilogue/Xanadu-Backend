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
 * @since 2023-06-04 05:08:31
 */
@Getter
@Setter
@TableName("ware_sub_input")
@ApiModel(value = "SubInput对象", description = "")
public class SubInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("入库记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("入库单号ID,可能为退货或调拨")
    @TableField("input_id")
    private Long inputId;

    @ApiModelProperty("入库类型，购货或是退单")
    @TableField("input_type")
    private String inputType;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("商品数量")
    @TableField("input_num")
    private Integer inputNum;

    @ApiModelProperty("入库日期")
    @TableField("input_time")
    private Date inputTime;

    @ApiModelProperty("子仓库ID")
    @TableField("subware_id")
    private Long subwareId;


}
