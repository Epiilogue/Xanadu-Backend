package edu.neu.sub.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 只需要填写拿不到的数据即可
 */
public class PaymentReceiptVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("客户姓名")
    private String name;

    @ApiModelProperty("客户联系电话")
    private String phone;

    @ApiModelProperty("分站ID")
    private Long substationId;

    @ApiModelProperty("任务类型")
    private String type;

    @ApiModelProperty("回执任务状态")
    private String state;

    @ApiModelProperty("配送员ID")
    private Long userId;

    @ApiModelProperty("客户满意度")
    private String feedback;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("送货/退货地址")
    private String address;

    @ApiModelProperty("签收时间")
    private Date signTime;

    @ApiModelProperty("回执录入日期")
    private Date createTime;

    @ApiModelProperty("计划商品数量")
    private Integer planNum;

    @ApiModelProperty("计划金额")
    private BigDecimal planReceipt;

    @ApiModelProperty("实际金额")
    private Integer actualReceipt;

    @ApiModelProperty("实际数量")
    private Integer actualNumber;

    @ApiModelProperty("发票号")
    private Long invoiceNumber;


}
