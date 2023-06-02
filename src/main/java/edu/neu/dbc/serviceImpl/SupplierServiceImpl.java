package edu.neu.dbc.serviceImpl;

import edu.neu.dbc.entity.Supplier;
import edu.neu.dbc.mapper.SupplierMapper;
import edu.neu.dbc.service.SupplierService;
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
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

}
