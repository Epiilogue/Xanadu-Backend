package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("cc_refund")
@ApiModel(value = "Refund对象", description = "")
public class Refund implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("操作订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty("原因")
    @TableField("reason")
    private String reason;

    @ApiModelProperty("操作类型(退货，换货,退订)")
    @TableField("operation_type")
    private String operationType;


}
