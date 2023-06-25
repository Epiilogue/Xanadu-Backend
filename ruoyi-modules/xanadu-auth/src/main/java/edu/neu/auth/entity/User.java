package edu.neu.auth.entity;

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
 * @author jin Zhang
 * @since 2023-06-25 10:59:21
 */
@Getter
@Setter
@TableName("user")
@ApiModel(value = "User", description = "用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("用户名")
    @TableField("username")
    private String userName;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("昵称")
    @TableField("nickname")
    private String nickName;

    @ApiModelProperty("头像")
    @TableField("headimage")
    private String headImage;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("职务")
    @TableField("job")
    private String job;

    @ApiModelProperty("权限")
    @TableField("auth")
    private String auth;

    @ApiModelProperty("创建时间")
    @TableField("createtime")
    private String createTime;
}
