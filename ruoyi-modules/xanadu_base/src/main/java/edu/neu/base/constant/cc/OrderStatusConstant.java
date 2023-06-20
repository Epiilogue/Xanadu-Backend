package edu.neu.base.constant.cc;

/**
 * 订单的状态常量，使用String类型明文存储
 * 无效、可分配、缺货、已完成,已调度，中心库房出库，配送站到货，已分配，已领货，已完成，失败,部分完成
 */
public class OrderStatusConstant {
    //可分配
    public static final String INVALID = "无效";
    //可分配
    public static final String CAN_BE_ALLOCATED = "可分配";
    //缺货
    public static final String OUT_OF_STOCK = "缺货";
    //已完成
    public static final String COMPLETED = "已完成";

    //已调度
    public static final String DISPATCHED = "已调度";
    //中心库房出库
    public static final String CENTER_OUTBOUND = "中心库房出库";
    //配送站到货
    public static final String SUBSTATION_ARRIVAL = "配送站到货";
    //已分配
    public static final String ALLOCATED = "已分配";
    //已领货
    public static final String RECEIVED = "已领货";
    //已完成
    public static final String FINISHED = "已完成";
    //失败
    public static final String FAILED = "失败";
    //部分完成
    public static final String PARTIAL_COMPLETED = "部分完成";

}
