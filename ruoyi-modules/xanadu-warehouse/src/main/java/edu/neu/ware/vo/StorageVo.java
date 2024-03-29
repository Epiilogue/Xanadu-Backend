package edu.neu.ware.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageVo implements Serializable {

    @ApiModelProperty("可分配库存量")
    private Integer availableNum;

    @ApiModelProperty("锁定库存量")
    private Integer lockedNum;

    @ApiModelProperty("已分配库存量")
    private Integer allocatedNum;

    @ApiModelProperty("已退货库存量")
    private Integer returnedNum;
    @ApiModelProperty("总库存量")
    private Integer totalNum;

}
