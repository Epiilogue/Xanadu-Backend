package edu.neu.dpc.service;

import edu.neu.dpc.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.neu.dpc.vo.OrderVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
public interface TaskService extends IService<Task> {

    String resolveTaskType(OrderVo orderVo);
}
