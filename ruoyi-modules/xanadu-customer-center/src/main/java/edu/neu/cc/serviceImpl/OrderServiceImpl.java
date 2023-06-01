package edu.neu.cc.serviceImpl;

import edu.neu.dto.OrderDto;
import edu.neu.mapper.OrderDtoMapper;
import edu.neu.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 10:32:09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDtoMapper, OrderDto> implements OrderService {

}
