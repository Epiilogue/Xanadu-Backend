package edu.neu.cc.serviceImpl;

import edu.neu.dto.StockoutDto;
import edu.neu.mapper.StockoutDtoMapper;
import edu.neu.service.StockoutService;
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
public class StockoutServiceImpl extends ServiceImpl<StockoutDtoMapper, StockoutDto> implements StockoutService {

}
