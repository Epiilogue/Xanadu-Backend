package edu.neu.base.constant.cc;


/**
 * 任务单的状态转化 已调度，可分配，已分配，已领货，已完成，失败 部分完成
 */
public class TaskStatus {

    public static final String SCHEDULED = "已调度";
    public static final String ASSIGNABLE = "可分配";
    public static final String ASSIGNED = "已分配";
    public static final String RECEIVED = "已领货";
    public static final String COMPLETED = "已完成";
    public static final String FAILED = "失败";

    public static final String PARTIAL_COMPLETED = "部分完成";

    public static final String DELIVERED = "执行完成";
}
