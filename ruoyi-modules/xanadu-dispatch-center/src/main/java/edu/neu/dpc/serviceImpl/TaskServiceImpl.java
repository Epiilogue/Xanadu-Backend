package edu.neu.dpc.serviceImpl;

import edu.neu.base.constant.cc.NewOrderType;
import edu.neu.base.constant.cc.OperationTypeConstant;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.dpc.entity.Task;
import edu.neu.dpc.mapper.TaskMapper;
import edu.neu.dpc.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.neu.dpc.vo.OrderVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-09 03:31:21
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Override
    public String resolveTaskType(OrderVo orderVo) {

        switch (orderVo.getOrderType()) {
            case OperationTypeConstant.ORDER:
                if (orderVo.getNewType().equals(NewOrderType.CASH_ON_DELIVERY))
                    return TaskType.PAYMENT_DELIVERY;
                if (orderVo.getNewType().equals(NewOrderType.PAYMENT_DELIVERY))
                    return TaskType.PAYMENT;
            case OperationTypeConstant.RETURN:
                return TaskType.RETURN;
            case OperationTypeConstant.EXCHANGE:
                return TaskType.EXCHANGE;
            default:
                break;
        }
        return null;
    }
}
