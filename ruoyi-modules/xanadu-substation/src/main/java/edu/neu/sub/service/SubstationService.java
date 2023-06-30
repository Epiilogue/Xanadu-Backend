package edu.neu.sub.service;

import edu.neu.sub.entity.Substation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分站 服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
public interface SubstationService extends IService<Substation> {


    List<Long> getCourierBySubstationId(Long subId);

    Long getSubstationIdByCourierId(Long courierId);

    List<Long> getAllSubstationManager();

    Substation getByManagerId(Long userId);

    void removeMasters(Long id);

    List<Long> getAllCourier();

    void addCourier(Long substationId, List<Long> courierIds);

    List<Long> getCourierList(Long substationId);

    int deleteCourier(Long substationId, Long courierId);

    Integer addMasters(Long id, List<Long> adminIds);

    List<Long> getSubstationMatsers(Long substationId);

    int deleteManager(Long substationId, Long userId);
}
