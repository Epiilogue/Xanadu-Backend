package edu.neu.sub.serviceImpl;

import edu.neu.sub.entity.Finance;
import edu.neu.sub.mapper.FinanceMapper;
import edu.neu.sub.service.FinanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品收款 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Service
public class FinanceServiceImpl extends ServiceImpl<FinanceMapper, Finance> implements FinanceService {

}
