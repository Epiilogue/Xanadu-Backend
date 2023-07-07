package edu.neu.ac.mapper;

import edu.neu.ac.entity.Invoice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 发票记录 Mapper 接口
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-26 10:34:41
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {

}
