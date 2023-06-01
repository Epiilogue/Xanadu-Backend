package edu.neu.cc.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
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
 * @since 2023-06-01 10:32:09
 */
@Getter
@Setter
@TableName("cc_customer")
@ApiModel(value = "CustomerDto对象", description = "")
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新日期")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty("客户姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty("身份证号")
    @TableField("identity_card")
    private String identityCard;

    @ApiModelProperty("收货地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("电话号码")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty("工作单位")
    @TableField("organization")
    private String organization;

    @ApiModelProperty("邮政编码")
    @TableField("postcode")
    private String postcode;

    @ApiModelProperty("是否被删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;


}
