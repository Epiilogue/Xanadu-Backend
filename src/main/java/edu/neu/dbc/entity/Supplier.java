package edu.neu.dbc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-06-02 11:12:09
 */
@Getter
@Setter
@TableName("dbc_supplier")
@ApiModel(value = "Supplier对象", description = "")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("供销商ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("供销商名字")
    @TableField("name")
    private String name;

    @ApiModelProperty("供销商地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("供销商联系人")
    @TableField("contact_person")
    private String contactPerson;

    @ApiModelProperty("供销商电话号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("供销商银行账户")
    @TableField("bank_account")
    private String bankAccount;

    @ApiModelProperty("备注")
    @TableField("remarks")
    private String remarks;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
