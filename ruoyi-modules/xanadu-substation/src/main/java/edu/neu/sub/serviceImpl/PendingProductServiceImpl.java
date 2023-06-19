package edu.neu.sub.serviceImpl;

import edu.neu.sub.entity.PendingProduct;
import edu.neu.sub.mapper.PendingProductMapper;
import edu.neu.sub.service.PendingProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-19 11:12:41
 */
@Service
public class PendingProductServiceImpl extends ServiceImpl<PendingProductMapper, PendingProduct> implements PendingProductService {

}
