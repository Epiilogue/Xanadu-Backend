package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
 * @since 2023-06-01 11:10:58
 */
@Getter
@Setter
@TableName("cc_new_order")
@ApiModel(value = "NewOrder对象", description = "")
public class NewOrder  implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的order主表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("电话号码")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty("备注信息")
    @TableField("comment")
    private String comment;

    @ApiModelProperty("预计送货日期")
    @TableField("delivery_time")
    private Date deliveryTime;

    @ApiModelProperty("是否要发票")
    @TableField("invoice_need")
    private Integer invoiceNeed;

    @ApiModelProperty("送货地址")
    @TableField("delivery_address")
    private String deliveryAddress;

    @ApiModelProperty("接收人姓名")
    @TableField("receiver_name")
    private String receiverName;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("是否删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("商品列表")
    @TableField(exist = false)
    private List<Product> products;


}
