package edu.neu.ac.serviceImpl;

import edu.neu.ac.entity.Supply;
import edu.neu.ac.mapper.SupplyMapper;
import edu.neu.ac.service.SupplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-23 09:35:39
 */
@Service
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements SupplyService {

}
