package edu.neu.dpc.serviceImpl;

import edu.neu.dpc.entity.Task;
import edu.neu.dpc.mapper.TaskMapper;
import edu.neu.dpc.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

}
