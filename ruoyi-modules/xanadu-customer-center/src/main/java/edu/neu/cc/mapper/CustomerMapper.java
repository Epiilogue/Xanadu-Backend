package edu.neu.cc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.neu.cc.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 11:10:58
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
