package edu.neu.ac.mapper;

import edu.neu.ac.entity.Invoices;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 分站发票管理 Mapper 接口
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-26 09:58:23
 */
@Mapper
public interface InvoicesMapper extends BaseMapper<Invoices> {

}
