package edu.neu.sub.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务，每晚远程调用。统计有当日每个分站的快递员数量，各种任务类型的数量，完成度数量，送货数量，收款额，退回数量，
 * 退款额，实收款，配送费，待缴款，是否缴款，能够查看历史是否结算等信息
 * 直接获取一个列表，这个需要保存到数据库，允许历史查看
 */
@Getter
@Setter
@TableName("sub_daily_report")
@ApiModel(value = "DailyReport", description = "每日报表记录")
@Data
public class DailyReport implements Serializable {

    @ApiModelProperty("ID")
    @TableField("id")
    private Long id;

    @ApiModelProperty("分站ID")
    @TableField("substation_id")
    private Long substationId;

    @ApiModelProperty("分站名称")
    @TableField("substation_name")
    private String substationName;

    @ApiModelProperty("统计时间")
    @TableField("statistic_time")
    private Date statisticTime;

    @ApiModelProperty("是否已缴款")
    @TableField("is_settled")
    private Boolean isSettled;

    @ApiModelProperty("快递员数量")
    @TableField("courier_num")
    private Integer courierNum;

    @ApiModelProperty("送货收款任务数")
    @TableField("delivery_receive_task_num")
    private Integer deliveryReceiveTaskNum;

    @ApiModelProperty("收款任务数")
    @TableField("receive_task_num")
    private Integer receiveTaskNum;

    @ApiModelProperty("退货任务数")
    @TableField("return_task_num")
    private Integer returnTaskNum;

    @ApiModelProperty("换货任务数")
    @TableField("exchange_task_num")
    private Integer exchangeTaskNum;

    @ApiModelProperty("送货任务数")
    @TableField("delivery_task_num")
    private Integer deliveryTaskNum;


    @ApiModelProperty("完成任务数")
    @TableField("finish_task_num")
    private Integer finishTaskNum;

    @ApiModelProperty("失败任务数")
    @TableField("fail_task_num")
    private Integer failTaskNum;

    @ApiModelProperty("部分完成任务数")
    @TableField("part_finish_task_num")
    private Integer partFinishTaskNum;

    @ApiModelProperty("签收数量")
    @TableField("sign_num")
    private Integer signNum;

    @ApiModelProperty("收款额")
    @TableField("receive")
    private Double receive;

    @ApiModelProperty("退回数量")
    @TableField("return_num")
    private Integer returnNum;

    @ApiModelProperty("退款额")
    @TableField("refund")
    private Double refund;

    @ApiModelProperty("配送费")
    @TableField("delivery_fee")
    private Double deliveryFee;

    @ApiModelProperty("待缴款")
    @TableField("to_pay")
    private Double toPay;

    @ApiModelProperty("今日满意度")
    @TableField("feedback")
    private Double feedback;

    public DailyReport(Long substationId, String substationName, Date statisticTime, Boolean isSettled) {

        this.substationId = substationId;
        this.substationName = substationName;
        this.statisticTime = statisticTime;
        this.isSettled = isSettled;
        //Integer 以及 Double赋予默认值
        this.courierNum = 0;
        this.deliveryReceiveTaskNum = 0;
        this.receiveTaskNum = 0;
        this.returnTaskNum = 0;
        this.exchangeTaskNum = 0;
        this.deliveryTaskNum = 0;
        this.finishTaskNum = 0;
        this.failTaskNum = 0;
        this.partFinishTaskNum = 0;
        this.signNum = 0;
        this.receive = 0.0;
        this.returnNum = 0;
        this.refund = 0.0;
        this.deliveryFee = 0.0;
        this.toPay = 0.0;
        this.feedback = 0.0;
    }

    public DailyReport() {
    }
}
