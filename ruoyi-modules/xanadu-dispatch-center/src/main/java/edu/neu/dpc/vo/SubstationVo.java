package edu.neu.dpc.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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
@ApiModel(value = "Substation对象", description = "分站")
public class SubstationVo implements Serializable {

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


    @ApiModelProperty("对应的管理员ID列表")
    @TableField(exist = false)
    private List<Long> adminIds;

}
