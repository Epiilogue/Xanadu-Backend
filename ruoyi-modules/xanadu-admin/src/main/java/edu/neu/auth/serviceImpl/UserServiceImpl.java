package edu.neu.auth.serviceImpl;

import edu.neu.auth.entity.User;
import edu.neu.auth.mapper.UserMapper;
import edu.neu.auth.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-25 10:59:21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
