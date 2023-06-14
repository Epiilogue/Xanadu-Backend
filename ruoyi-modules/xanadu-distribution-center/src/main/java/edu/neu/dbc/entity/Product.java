package edu.neu.dbc.entity;

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
 * @since 2023-06-02 11:12:09
 */
@Getter
@Setter
@TableName("dbc_product")
@ApiModel(value = "Product对象", description = "")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品名称")
    @TableField("name")
    private String name;

    @TableField(exist = false)
    private Categary categary;

    @ApiModelProperty("一级分类ID")
    @TableField("first_categray")
    private Integer firstCategray;

    @ApiModelProperty("二级分类ID")
    @TableField("second_categray")
    private Integer secondCategray;

    @ApiModelProperty("商品价格")
    @TableField("price")
    private Double price;

    @ApiModelProperty("商品成本")
    @TableField("cost")
    private Double cost;

    @ApiModelProperty("供销商ID")
    @TableField("supplier_id")
    private Long supplierId;

    @ApiModelProperty("能否退货")
    @TableField("refund_able")
    private Boolean refundAble;

    @ApiModelProperty("能否换货")
    @TableField("change_able")
    private Boolean changeAble;

    @ApiModelProperty("备注")
    @TableField("comment")
    private String comment;

    @ApiModelProperty("是否已被删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("商品图片url")
    @TableField("picture")
    private String picture;

    @ApiModelProperty("商品安全库存")
    @TableField("safe_stock")
    private Integer safeStock;

    @ApiModelProperty("货物最大库存量")
    @TableField("max_count")
    private Integer maxCount;


}
