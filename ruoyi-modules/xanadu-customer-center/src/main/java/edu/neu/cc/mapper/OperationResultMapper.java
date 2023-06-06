package edu.neu.cc.mapper;

import edu.neu.cc.vo.OperationResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface OperationResultMapper {


    List<OperationResultVo> listOperationResult(Long userId, Date startTime, Date endTime);
}
