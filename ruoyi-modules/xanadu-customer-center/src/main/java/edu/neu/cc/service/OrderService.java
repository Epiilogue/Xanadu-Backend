package edu.neu.cc.service;

import edu.neu.cc.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
public interface OrderService extends IService<Order> {

    Boolean batchUpdateStatus(String status, List<Long> orderIdList);
}
