package edu.neu.invoice.serviceImpl;

import edu.neu.invoice.entity.Invoice;
import edu.neu.invoice.mapper.InvoiceMapper;
import edu.neu.invoice.service.InvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发票记录 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-08 09:58:46
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

}
