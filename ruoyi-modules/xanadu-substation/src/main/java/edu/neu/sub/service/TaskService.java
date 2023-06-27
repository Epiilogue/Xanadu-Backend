package edu.neu.sub.service;

import edu.neu.sub.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 任务单 服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 04:10:48
 */
public interface TaskService extends IService<Task> {

    List<Task> getTasksByCourierId(Long courierId);

    List<Task> getTodayTasks(Date start, Date now);

}
