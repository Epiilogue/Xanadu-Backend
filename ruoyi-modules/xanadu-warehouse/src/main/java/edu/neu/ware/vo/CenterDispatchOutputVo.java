package edu.neu.ware.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 调拨单号、 商品代码、商品名称、出库数量、 任务号、 日期
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenterDispatchOutputVo implements Serializable {


    @ApiModelProperty("中心仓库出库记录ID")
    private Long id;

    @ApiModelProperty("调拨单号")
    private Long outputId;

    @ApiModelProperty("商品代码")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("出库数量")
    private Integer ouputNum;

    @ApiModelProperty("出库状态")
    private String status;

    @ApiModelProperty("分库ID")
    private Long targetId;

    @ApiModelProperty("任务号")
    private Long taskId;

    @ApiModelProperty("要求日期")
    private Date requireTime;

    @ApiModelProperty("出库日期")
    private Date outputTime;
}
