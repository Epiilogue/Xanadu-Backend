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
 * @author Gaosong Xu
 * @since 2023-06-23 09:35:39
 */
@Service
public class InvoicesServiceImpl extends ServiceImpl<InvoicesMapper, Invoices> implements InvoicesService {

}
