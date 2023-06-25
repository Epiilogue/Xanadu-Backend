package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import edu.neu.base.constant.cc.TaskType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Finance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("送货数量")
    @TableField("delivery_num")
    private Integer deliveryNum;

    @ApiModelProperty("应收额")
    @TableField("receive")
    private Double receive;

    @ApiModelProperty("签收数量")
    @TableField("sign_num")
    private Integer signNum;

    @ApiModelProperty("退回数量")
    @TableField("return_num")
    private Integer returnNum;

    @ApiModelProperty("退款额")
    @TableField("refund")
    private Double refund;

    @ApiModelProperty("实收额")
    @TableField("actual")
    private Double actual;

    @ApiModelProperty("应缴额")
    @TableField("pay")
    private Double pay;

    public void update(Receipt r, ReceiptProduct p) {
        String taskType = r.getTaskType();
        switch (taskType) {
            //完成送货任务
            case TaskType.DELIVERY:
            case TaskType.PAYMENT_DELIVERY:
                this.deliveryNum += p.getAllNum();
                this.receive += p.getAllNum() * p.getPrice();
                this.signNum += p.getSignNum();
                this.returnNum += p.getReturnNum();
                this.refund += p.getOutputMoney();
                break;
            //完成换货任务,不退钱
            case TaskType.EXCHANGE:
                this.deliveryNum += p.getAllNum();
                this.signNum += p.getSignNum();
                this.returnNum += p.getReturnNum();
                break;
            //完成退货任务
            case TaskType.RETURN:
                this.returnNum += p.getReturnNum();
                this.refund += p.getOutputMoney();
                break;
        }
    }
}
