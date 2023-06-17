package edu.neu.sub.mapper;

import edu.neu.sub.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 任务单 Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 04:10:48
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}
