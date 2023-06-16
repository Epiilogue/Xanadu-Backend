package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 分站
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Getter
@Setter
@TableName("sub_substation")
@ApiModel(value = "Substation对象", description = "分站")
public class Substation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分站ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("分站名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("分站地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("分站电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("分站对应的分仓库的ID")
    @TableField("subware_id")
    private Long subwareId;

    @ApiModelProperty("管理人ID")
    @TableField("user_id")
    private Long userId;


}
