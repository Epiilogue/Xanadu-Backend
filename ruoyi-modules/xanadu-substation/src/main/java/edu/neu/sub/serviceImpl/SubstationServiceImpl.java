package edu.neu.sub.serviceImpl;

import edu.neu.sub.entity.Substation;
import edu.neu.sub.mapper.SubstationMapper;
import edu.neu.sub.service.SubstationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分站 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Service
public class SubstationServiceImpl extends ServiceImpl<SubstationMapper, Substation> implements SubstationService {

    @Autowired
    SubstationMapper substationMapper;


    @Override
    public List<Long> getCourierBySubstationId(Long subId) {
        return substationMapper.getCourierBySubstationId(subId);
    }

    @Override
    public Long getSubstationIdByCourierId(Long courierId){
        return substationMapper.getSubstationIdByCourierId(courierId);
    }

    @Override
    public List<Long> getAllSubstationManager() {
        return substationMapper.getAllSubstationManager();
    }

    @Override
    public Substation getByManagerId(Long userId) {
        return substationMapper.getByManagerId(userId);
    }

    @Override
    public void removeMasters(Long id) {
        substationMapper.removeMasters(id);
    }

    @Override
    public List<Long> getAllCourier() {
        return substationMapper.getAllCourier();
    }

    @Override
    public void addCourier(Long substationId, List<Long> courierIds) {
       courierIds.forEach(courierId -> substationMapper.addCourier(substationId, courierId));
    }

    @Override
    public List<Long> getCourierList(Long substationId) {
        return substationMapper.getCourierList(substationId);
    }

    @Override
    public int deleteCourier(Long substationId, Long courierId) {
        return substationMapper.deleteCourier(substationId, courierId);
    }

    @Override
    public Integer addMasters(Long id, List<Long> adminIds) {
        int count = 0;
        for (Long adminId : adminIds) {
            count += substationMapper.addMaster(id, adminId);
        }
        return count;
    }

    @Override
    public List<Long> getSubstationMatsers(Long substationId) {
        return substationMapper.getSubstationMasters(substationId);
    }

    @Override
    public int deleteManager(Long substationId, Long userId) {
        return substationMapper.deleteManager(substationId, userId);
    }
}
