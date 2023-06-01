package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2023-06-01 11:10:58
 */
@Getter
@Setter
@TableName("cc_stockout")
@ApiModel(value = "Stockout对象", description = "")
public class Stockout implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("缺货记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("缺货订单号")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("缺货商品号")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("缺货商品当前数量")
    @TableField("now_numbers")
    private Integer nowNumbers;

    @ApiModelProperty("缺货商品数量")
    @TableField("need_numbers")
    private Integer needNumbers;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("创建客服")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty("缺货状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("是否删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
