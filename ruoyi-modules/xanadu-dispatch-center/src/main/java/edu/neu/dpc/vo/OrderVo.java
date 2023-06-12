package edu.neu.dpc.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import edu.neu.dpc.entity.Product;
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

    @ApiModelProperty("操作人ID")
    private Long userId;

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

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("送货地址")
    private String deliveryAddress;


    @ApiModelProperty("商品列表")
    private List<Product> products;

    @ApiModelProperty("新订单类型")
    private String newType;

}