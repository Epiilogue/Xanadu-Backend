package edu.neu.cc.serviceImpl;

import edu.neu.cc.entity.Stockout;
import edu.neu.cc.mapper.StockoutMapper;
import edu.neu.cc.service.StockoutService;
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
public class StockoutServiceImpl extends ServiceImpl<StockoutMapper, Stockout> implements StockoutService {

}
