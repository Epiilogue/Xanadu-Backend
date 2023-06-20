package edu.neu.ware.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Gaosong Xu
 * @since 2023-06-19 11:12:41
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PendingProductVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品单价")
    private Double productPrice;

    @ApiModelProperty("待处理数量")
    private Integer dealNumber;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("子库ID")
    private Long subwareId;

}
