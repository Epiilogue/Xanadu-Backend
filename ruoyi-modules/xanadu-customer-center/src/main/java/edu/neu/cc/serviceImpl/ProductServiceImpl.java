package edu.neu.cc.serviceImpl;

import edu.neu.dto.ProductDto;
import edu.neu.mapper.ProductDtoMapper;
import edu.neu.service.ProductService;
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
public class ProductServiceImpl extends ServiceImpl<ProductDtoMapper, ProductDto> implements ProductService {

}
