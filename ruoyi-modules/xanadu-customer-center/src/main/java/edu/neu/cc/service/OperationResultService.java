package edu.neu.cc.service;

import edu.neu.cc.vo.OperationResultVo;

import java.util.Date;
import java.util.List;


public interface OperationResultService {

    /**
     * 传入用户ID，开始时间，结束时间，操作类型，返回操作结果列表，返回的结果为商品名，商品总价值，商品数量等信息
     */
    List<OperationResultVo> listOperationResult(Long userId, Date startTime, Date endTime);
}
