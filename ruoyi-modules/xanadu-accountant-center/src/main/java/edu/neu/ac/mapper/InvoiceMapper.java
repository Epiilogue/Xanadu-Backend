package edu.neu.ac.mapper;

import edu.neu.ac.entity.Invoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 发票记录 Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-23 09:35:39
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {

}
