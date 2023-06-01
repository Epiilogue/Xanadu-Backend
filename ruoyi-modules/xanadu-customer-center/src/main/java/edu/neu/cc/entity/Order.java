package edu.neu.cc.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 10:32:09
 */
@Data
@ApiModel(value = "Order对象", description = "")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("操作人ID")
    private Long userId;

    @ApiModelProperty("客户信息")
    private Customer customer;

    @ApiModelProperty("创建日期")
    private Date createTime;

    @ApiModelProperty("订单类型")
    private String orderType;

    @ApiModelProperty("要求完成日期")
    private Date deadline;

}
