package edu.neu.sub.mapper;

import edu.neu.sub.entity.Receipt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 回执单 Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {

}
