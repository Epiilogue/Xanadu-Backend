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
 * @author jin Zhang
 * @since 2023-06-26 09:58:23
 */
@Service
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements SupplyService {

}
