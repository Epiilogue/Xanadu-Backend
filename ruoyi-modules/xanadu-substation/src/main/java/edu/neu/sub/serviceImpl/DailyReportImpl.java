package edu.neu.sub.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.neu.sub.entity.DailyReport;
import edu.neu.sub.entity.Delivery;
import edu.neu.sub.mapper.DailyReportMapper;
import edu.neu.sub.mapper.DeliveryMapper;
import edu.neu.sub.service.DailyReportService;
import edu.neu.sub.service.DeliveryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投递费结算 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Service
public class DailyReportImpl extends ServiceImpl<DailyReportMapper, DailyReport> implements DailyReportService {

}
