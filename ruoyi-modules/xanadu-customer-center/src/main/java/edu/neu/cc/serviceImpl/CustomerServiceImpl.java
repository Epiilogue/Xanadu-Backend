package edu.neu.cc.serviceImpl;

import edu.neu.dto.CustomerDto;
import edu.neu.mapper.CustomerDtoMapper;
import edu.neu.service.CustomerService;
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
public class CustomerServiceImpl extends ServiceImpl<CustomerDtoMapper, CustomerDto> implements CustomerService {

}
