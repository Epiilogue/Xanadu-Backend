package edu.neu.sub.serviceImpl;

import edu.neu.sub.entity.Receipt;
import edu.neu.sub.mapper.ReceiptMapper;
import edu.neu.sub.service.ReceiptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 回执单 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Service
public class ReceiptServiceImpl extends ServiceImpl<ReceiptMapper, Receipt> implements ReceiptService {

}
