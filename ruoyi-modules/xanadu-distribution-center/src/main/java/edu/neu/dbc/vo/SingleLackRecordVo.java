package edu.neu.dbc.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Api("单条缺货记录，需要远程调用获取")
public class SingleLackRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("缺货记录ID")
    private Long id;

    @ApiModelProperty("缺货订单号")
    private Long orderId;


    @ApiModelProperty("缺货商品数量")
    private Integer needNumbers;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建客服")
    private Long createBy;

    @ApiModelProperty("来源")
    private String source;

}
