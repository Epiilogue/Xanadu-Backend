package edu.neu.ac.serviceImpl;

import edu.neu.ac.entity.Invoice;
import edu.neu.ac.mapper.InvoiceMapper;
import edu.neu.ac.service.InvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发票记录 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-23 09:35:39
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

}
