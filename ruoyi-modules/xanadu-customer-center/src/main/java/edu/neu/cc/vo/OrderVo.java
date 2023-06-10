package edu.neu.cc.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import edu.neu.cc.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//订单号、客户姓名、投递地址、要求完成日期、订单类型
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    private Long id;


    @ApiModelProperty("客户ID")
    private Long customerId;

    @ApiModelProperty("创建日期")
    private Date createTime;

    @ApiModelProperty("订单类型")
    private String orderType;

    @ApiModelProperty("要求完成日期")
    private Date deadline;

    @ApiModelProperty("涉及的金额")
    private Double totalAmount;


    @ApiModelProperty("涉及的商品数量")
    private Integer numbers;

    @ApiModelProperty("订单状态")
    private String status;


    @ApiModelProperty("接收人姓名")
    private String receiverName;

    @ApiModelProperty("投递地址")
    private String deliveryAddress;


    @ApiModelProperty("商品列表")
    private List<Product> products;

}