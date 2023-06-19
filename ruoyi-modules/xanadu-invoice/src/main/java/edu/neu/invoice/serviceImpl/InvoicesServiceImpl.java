package edu.neu.invoice.serviceImpl;

import edu.neu.invoice.entity.Invoices;
import edu.neu.invoice.mapper.InvoicesMapper;
import edu.neu.invoice.service.InvoicesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分站发票管理 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-08 09:58:46
 */
@Service
public class InvoicesServiceImpl extends ServiceImpl<InvoicesMapper, Invoices> implements InvoicesService {

}
