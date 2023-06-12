package edu.neu.dpc.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DispatchVo implements Serializable {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty("调拨单号")
    private Long id;

    @ApiModelProperty("入库分库ID")
    private Long subwareId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品数量")
    private Integer productNum;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("计划出库时间")
    private Date planTime;

    @ApiModelProperty("调度状态")
    private String status;


    @ApiModelProperty("商品大类")
    private String productCategary;

    @ApiModelProperty("可分配库存量")
    private Integer availableNum;

    @ApiModelProperty("锁定库存量")
    private Integer lockedNum;

    @ApiModelProperty("已分配库存量")
    private Integer allocatedNum;

    @ApiModelProperty("总库存量")
    private Integer totalNum;

}
