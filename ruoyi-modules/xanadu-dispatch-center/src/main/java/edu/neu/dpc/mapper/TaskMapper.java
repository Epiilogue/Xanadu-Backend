package edu.neu.dpc.mapper;

import edu.neu.dpc.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}
