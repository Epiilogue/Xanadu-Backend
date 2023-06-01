package edu.neu.cc.serviceImpl;

import com.alibaba.cloud.commons.lang.StringUtils;
import edu.neu.cc.entity.Customer;
import edu.neu.cc.mapper.CustomerMapper;
import edu.neu.cc.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {



}
