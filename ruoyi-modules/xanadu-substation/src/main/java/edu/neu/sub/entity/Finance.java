package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品收款
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Getter
@Setter
@TableName("sub_finance")
@ApiModel(value = "Finance对象", description = "商品收款")
public class Finance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("送货数量")
    @TableField("delivery_num")
    private Integer deliveryNum;

    @ApiModelProperty("退回数量")
    @TableField("return_num")
    private Integer returnNum;

    @ApiModelProperty("退款额")
    @TableField("refund")
    private Double refund;

    @ApiModelProperty("应收额")
    @TableField("receive")
    private Double receive;

    @ApiModelProperty("实收额")
    @TableField("actual")
    private Double actual;

    @ApiModelProperty("应缴额")
    @TableField("pay")
    private Double pay;

    @ApiModelProperty("是否已删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
