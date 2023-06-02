package edu.neu.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("ware_subware")
@ApiModel(value = "Subware对象", description = "")
public class Subware implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分库房ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("库房名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("库房地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("经度")
    @TableField("x")
    private Double x;

    @ApiModelProperty("纬度")
    @TableField("y")
    private Double y;

    @ApiModelProperty("库管员")
    @TableField("master")
    private Long master;


}
