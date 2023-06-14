package edu.neu.dpc.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
@Getter
@Setter
@TableName("ware_center_output")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CenterOutput对象", description = "")
public class CenterOutputVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("退货出库或调拨出库的ID")
    private Long outputId;

    @ApiModelProperty("任务单ID")
    private Long taskId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("出库数量")
    private Integer ouputNum;

    @ApiModelProperty("出库类型")
    private String outputType;

    @ApiModelProperty("要求出库时间")
    private Date requireTime;
    ;
    @ApiModelProperty("供应商ID")
    private Long supplierId;

    @ApiModelProperty("分站ID")
    private Long substationId;

    @ApiModelProperty("分库ID")
    private Long subwareId;


}
