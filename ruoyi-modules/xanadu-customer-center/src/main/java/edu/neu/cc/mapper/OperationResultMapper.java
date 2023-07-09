package edu.neu.cc.mapper;

import edu.neu.cc.vo.OperationResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OperationResultMapper {


    List<OperationResultVo> listOperationResult(@Param("userId") Long userId, @Param("startTime")Date startTime, @Param("endTime")Date endTime);
}
