package edu.neu.sub.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 *   任务单号、 姓名、联系电话、 任务分站、任务类型、送货地址，
 *   任务状态、客户满意度、 备注 ，配送员，总额、客户满意度、备注
 */

public class PaymentReceiptVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("回执任务状态，完成或者失败")
    private String state;

    @ApiModelProperty("客户满意度,0-10")
    private Integer feedback;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("签收时间")
    private Date signTime;


}
