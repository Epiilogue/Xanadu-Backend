package edu.neu.ware.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *  商品分类、商品代码、商品名称、供应商、计量单位、分站名称、退货数量、 日期
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CenterRefundOutputVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("中心仓库出库记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("退货出库单ID")
    @TableField("output_id")
    private Long outputId;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("出库数量")
    @TableField("output_num")
    private Integer outputNum;

    @ApiModelProperty("出库时间")
    @TableField("output_time")
    private Date outputTime;

    @ApiModelProperty("出库状态")
    @TableField("status")
    private String status;

    @ApiModelProperty("供应商ID")
    @TableField("supplier_id")
    private Long supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;


    @ApiModelProperty("要求出库时间")
    @TableField("require_time")
    private Date requireTime;

    @ApiModelProperty("实际数量")
    private Integer actualNum;

}
