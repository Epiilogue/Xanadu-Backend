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

    @ApiModelProperty("回执任务状态，完成或者失败")
    private String state;

    @ApiModelProperty("客户满意度")
    private String feedback;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("签收时间")
    private Date signTime;


}
