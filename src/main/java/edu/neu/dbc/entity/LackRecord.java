package edu.neu.dbc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@Getter
@Setter
@TableName("dbc_lack_record")
@ApiModel(value = "LackRecord对象", description = "")
public class LackRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("缺货单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("分类ID")
    @TableField("categary_id")
    private Integer categaryId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("当前库存数量")
    @TableField("now_count")
    private Integer nowCount;

    @ApiModelProperty("安全库存数量")
    @TableField("safe_count")
    private Integer safeCount;

    @ApiModelProperty("缺货数量")
    @TableField("need_count")
    private Integer needCount;

    @ApiModelProperty("进货数量")
    @TableField("input_count")
    private Integer inputCount;

    @ApiModelProperty("创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}
