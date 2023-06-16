package edu.neu.sub.serviceImpl;

import edu.neu.sub.entity.ReceiptProduct;
import edu.neu.sub.mapper.ReceiptProductMapper;
import edu.neu.sub.service.ReceiptProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单商品的签收情况 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Service
public class ReceiptProductServiceImpl extends ServiceImpl<ReceiptProductMapper, ReceiptProduct> implements ReceiptProductService {

}
