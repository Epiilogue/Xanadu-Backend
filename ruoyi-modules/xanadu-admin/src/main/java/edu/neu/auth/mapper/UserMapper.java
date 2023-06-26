package edu.neu.auth.mapper;

import edu.neu.auth.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jin Zhang
 * @since 2023-06-25 10:59:21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
