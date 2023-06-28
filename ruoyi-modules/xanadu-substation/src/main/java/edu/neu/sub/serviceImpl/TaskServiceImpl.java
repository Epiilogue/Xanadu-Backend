package edu.neu.sub.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.neu.base.constant.cc.TaskStatus;
import edu.neu.sub.entity.Task;
import edu.neu.sub.mapper.TaskMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.neu.sub.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 任务单 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 04:10:48
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    TaskMapper taskMapper;

    @Override
    public List<Task> getTasksByCourierId(Long courierId) {
        QueryWrapper<Task> eq = new QueryWrapper<Task>().eq("courier_id", courierId).eq("status", TaskStatus.ASSIGNED);
        return this.list(eq);
    }

    @Override
    public List<Task> getTodayTasks(Date start, Date now) {
        return taskMapper.getTodayTasks(start, now);
    }
}
