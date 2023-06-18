package edu.neu.sub.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptVo implements Serializable {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("回执任务状态，完成或者部分完成或者失败")
    private String state;

    @ApiModelProperty("客户满意度,0-10")
    private Integer feedback;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("签收时间")
    private Date signTime;

    @ApiModelProperty("商品列表,里面封装了实际签收的商品数量")
    private List<ProductVo> products;

}
