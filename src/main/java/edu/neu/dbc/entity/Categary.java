package edu.neu.dbc.entity;

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
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@Getter
@Setter
@TableName("dbc_categary")
@ApiModel(value = "Categary对象", description = "")
public class Categary implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("大类ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父节点ID")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty("节点层级")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("分类名称")
    @TableField("category")
    private String category;


}
