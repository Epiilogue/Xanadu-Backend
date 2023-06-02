package edu.neu.dbc.serviceImpl;

import edu.neu.dbc.entity.Product;
import edu.neu.dbc.mapper.ProductMapper;
import edu.neu.dbc.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
