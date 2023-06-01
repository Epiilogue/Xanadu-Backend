package edu.neu.cc.serviceImpl;

import edu.neu.dto.NewOrderDto;
import edu.neu.mapper.NewOrderDtoMapper;
import edu.neu.service.NewOrderService;
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
public class NewOrderServiceImpl extends ServiceImpl<NewOrderDtoMapper, NewOrderDto> implements NewOrderService {

}
