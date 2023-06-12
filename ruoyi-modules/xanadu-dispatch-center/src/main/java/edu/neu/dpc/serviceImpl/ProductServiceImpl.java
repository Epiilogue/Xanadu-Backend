package edu.neu.dpc.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.neu.dpc.entity.Product;
import edu.neu.dpc.mapper.ProductMapper;
import edu.neu.dpc.service.ProductService;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
