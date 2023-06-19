package edu.neu.invoice.mapper;

import edu.neu.invoice.entity.Invoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 发票记录 Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-08 09:58:46
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {

}
