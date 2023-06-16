package edu.neu.sub.mapper;

import edu.neu.sub.entity.ReceiptProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单商品的签收情况 Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Mapper
public interface ReceiptProductMapper extends BaseMapper<ReceiptProduct> {

}
