package edu.neu.ware.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
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
@ApiModel(value = "CenterOutput对象", description = "")
public class CenterOutputVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("退货出库或调拨出库的ID")
    private Long outputId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("出库数量")
    private Integer ouputNum;

    @ApiModelProperty("出库类型")
    private String outputType;

    @ApiModelProperty("出库时间")
    private Date outputTime;

    @ApiModelProperty("出库状态")
    private String status;

    @ApiModelProperty("目标ID")
    private Long targetId;

}
