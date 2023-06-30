package edu.neu.ware.mapper;

import edu.neu.ware.entity.Subware;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
@Mapper
public interface SubwareMapper extends BaseMapper<Subware> {

    List<Long> getAllSubwareManager();

    Long getSubwareIdByManagerId(Long managerId);

    List<Long> getSubwareMatsers(Long subwareId);

    int addMaster(@Param("subwareId") Long subwareId,@Param("masterId") Long masterId);

    void removeMasters(Long subwareId);
}
