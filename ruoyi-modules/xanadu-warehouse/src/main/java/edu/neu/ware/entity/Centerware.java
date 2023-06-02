package edu.neu.ware.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 03:42:21
 */
@Getter
@Setter
@TableName("ware_centerware")
@ApiModel(value = "Centerware对象", description = "")
public class Centerware implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("仓库名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("中心仓库地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("中心仓库经度")
    @TableField("x")
    private Double x;

    @ApiModelProperty("中心仓库纬度")
    @TableField("y")
    private Double y;

    @ApiModelProperty("仓库预警值")
    @TableField("warn_number")
    private Integer warnNumber;

    @ApiModelProperty("仓库最高值")
    @TableField("max_number")
    private Integer maxNumber;

    @ApiModelProperty("仓库城市位置")
    @TableField("city")
    private String city;


}
