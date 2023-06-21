package edu.neu.cc.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.cc.service.OperationResultService;
import edu.neu.cc.vo.OperationContactResultVo;
import edu.neu.cc.vo.OperationResultVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cc/operation")
public class OperationResultController {

    @Autowired
    OperationResultService operationResultService;


    @ApiOperation(value = "传入用户ID，开始时间，结束时间，操作类型，返回操作结果列表，返回的结果为商品名，商品总价值，商品数量等信息")
    @PostMapping("/listOperationResult/{userId}")
    public AjaxResult listOperationResult(Long userId, @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startTime, @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endTime) {
        //查询一次，获取到所有商品的信息，然后需要合并相同的商品，使用stream map合并至新的list
        List<OperationResultVo> operationResultVos = operationResultService.listOperationResult(userId, startTime, endTime);
        Map<Long, OperationContactResultVo> map = new HashMap<>();

        for (OperationResultVo operationResultVo : operationResultVos) {
            Long productId = operationResultVo.getProductId();
            OperationContactResultVo operationContactResultVo = map.get(productId);
            if (operationContactResultVo == null) {
                OperationContactResultVo tmpResult = new OperationContactResultVo();
                tmpResult.setUserId(userId);
                tmpResult.setProductName(operationResultVo.getProductName());
                tmpResult.setProductType(operationResultVo.getProductType());
                tmpResult.setProductId(operationResultVo.getProductId());
                //初始化，避免空指针引用
                tmpResult.setNewCount(0);
                tmpResult.setNewNumber(0);
                tmpResult.setNewAmount(0.0);
                tmpResult.setReturnCount(0);
                tmpResult.setReturnNumber(0);
                tmpResult.setReturnAmount(0.0);
                tmpResult.setExchangeCount(0);
                tmpResult.setExchangeNumber(0);
                tmpResult.setCancelCount(0);
                tmpResult.setCancelNumber(0);
                tmpResult.setCancelAmount(0.0);
                operationContactResultVo = tmpResult;
                map.put(productId, tmpResult);
            }
            //判断订单类型,修改对应的值
            String orderType = operationResultVo.getOrderType();
            switch (orderType) {
                case OperationTypeConstant.ORDER:
                    operationContactResultVo.setNewCount(operationContactResultVo.getNewCount() + operationResultVo.getCount());
                    operationContactResultVo.setNewNumber(operationContactResultVo.getNewNumber() + operationResultVo.getNumber());
                    operationContactResultVo.setNewAmount(operationContactResultVo.getNewAmount() + operationResultVo.getAmount());
                    break;
                case OperationTypeConstant.RETURN:
                    operationContactResultVo.setReturnCount(operationContactResultVo.getReturnCount() + operationResultVo.getCount());
                    operationContactResultVo.setReturnNumber(operationContactResultVo.getReturnNumber() + operationResultVo.getNumber());
                    operationContactResultVo.setReturnAmount(operationContactResultVo.getReturnAmount() + operationResultVo.getAmount());
                    break;
                case OperationTypeConstant.EXCHANGE:
                    operationContactResultVo.setExchangeCount(operationContactResultVo.getExchangeCount() + operationResultVo.getCount());
                    operationContactResultVo.setExchangeNumber(operationContactResultVo.getExchangeNumber() + operationResultVo.getNumber());
                    break;
                case OperationTypeConstant.UNSUBSCRIBE:
                    operationContactResultVo.setCancelCount(operationContactResultVo.getCancelCount() + operationResultVo.getCount());
                    operationContactResultVo.setCancelNumber(operationContactResultVo.getCancelNumber() + operationResultVo.getNumber());
                    operationContactResultVo.setCancelAmount(operationContactResultVo.getCancelAmount() + operationResultVo.getAmount());
                    break;
                default:
                    break;
            }
        }
        return AjaxResult.success(new ArrayList<>(map.values()));
    }


}

