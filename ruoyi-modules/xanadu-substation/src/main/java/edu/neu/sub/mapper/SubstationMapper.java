package edu.neu.sub.mapper;

import edu.neu.sub.entity.Substation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 分站 Mapper 接口
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Mapper
public interface SubstationMapper extends BaseMapper<Substation> {

    List<Long> getCourierBySubstationId(Long subId);

    Long getSubstationIdByCourierId(Long courierId);

    List<Long> getAllSubstationManager();

    Substation getByManagerId(Long userId);

    void removeMasters(Long subId);

    List<Long> getAllCourier();

    void addCourier(@Param("substationId") Long substationId,@Param("courierId") Long courierId);

    List<Long> getCourierList(Long substationId);

    int deleteCourier(@Param("substationId")Long substationId,@Param("courierId") Long courierId);

    int addMaster(@Param("subId")Long subId,@Param("masterId") Long masterId);

    List<Long> getSubstationMasters(Long substationId);

    int deleteManager(@Param("substationId")Long substationId,@Param("userId") Long userId);
}
