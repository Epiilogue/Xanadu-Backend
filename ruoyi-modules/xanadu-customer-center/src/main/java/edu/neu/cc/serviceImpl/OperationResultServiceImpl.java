package edu.neu.cc.serviceImpl;

import edu.neu.cc.mapper.OperationResultMapper;
import edu.neu.cc.service.OperationResultService;
import edu.neu.cc.service.OperationService;
import edu.neu.cc.vo.OperationResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OperationResultServiceImpl implements OperationResultService {

    @Autowired
    private OperationResultMapper operationResultMapper;

    @Override
    public List<OperationResultVo> listOperationResult(Long userId, Date startTime, Date endTime) {
        return operationResultMapper.listOperationResult(userId, startTime, endTime);
    }
}
