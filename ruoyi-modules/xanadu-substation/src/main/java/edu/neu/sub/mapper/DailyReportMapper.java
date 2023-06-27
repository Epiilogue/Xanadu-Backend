package edu.neu.sub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.neu.sub.entity.DailyReport;
import edu.neu.sub.entity.Finance;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品收款 Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Mapper
public interface DailyReportMapper extends BaseMapper<DailyReport> {

}
