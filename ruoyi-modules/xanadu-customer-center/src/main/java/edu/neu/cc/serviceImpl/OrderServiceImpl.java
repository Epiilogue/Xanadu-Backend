package edu.neu.cc.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.neu.cc.entity.Order;
import edu.neu.cc.mapper.OrderMapper;
import edu.neu.cc.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    OrderMapper orderMapper;


    @Override
    public Boolean batchUpdateStatus(String status, List<Long> orderIdList) {
        int update = orderMapper.update(null, new UpdateWrapper<Order>().set("status", status).in("id", orderIdList));
        return update > 0;
    }
}
