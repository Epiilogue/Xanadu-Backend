package edu.neu.sub.entity;

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
 * 投递费结算
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Getter
@Setter
@ApiModel(value = "Delivery对象", description = "投递费结算")
//待完成任务数，已完成总任务数，今日完成数，今日待结配送费，计算公式,平均满意度
public class Delivery implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("投递员ID")
    private Long courierId;

    @ApiModelProperty("分站ID")
    private Long substationId;

    @ApiModelProperty("待完成任务数")
    private Integer unfinishedTask;

    @ApiModelProperty("已完成总任务数")
    private Integer finishedTask;

    @ApiModelProperty("今日完成数")
    private Integer todayFinishedTask;

    @ApiModelProperty("今日待结配送费,每一单实收款*(1.0+(客户满意度-0.5))")
    private Double todayDeliveryFee;

    @ApiModelProperty("平均满意度")
    private Double averageSatisfaction;


}
