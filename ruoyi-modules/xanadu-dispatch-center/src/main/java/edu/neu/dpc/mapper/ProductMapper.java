package edu.neu.dpc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import edu.neu.dpc.entity.Product;
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
public interface ProductMapper extends BaseMapper<Product> {

}
