package edu.neu.ac.serviceImpl;

import edu.neu.ac.entity.Invoices;
import edu.neu.ac.mapper.InvoicesMapper;
import edu.neu.ac.service.InvoicesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分站发票管理 服务实现类
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-26 09:58:23
 */
@Service
public class InvoicesServiceImpl extends ServiceImpl<InvoicesMapper, Invoices> implements InvoicesService {

}
