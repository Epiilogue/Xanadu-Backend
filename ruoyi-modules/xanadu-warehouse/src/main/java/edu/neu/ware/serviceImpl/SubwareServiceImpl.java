package edu.neu.ware.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.mapper.SubwareMapper;
import edu.neu.ware.service.SubwareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
@Service
public class SubwareServiceImpl extends ServiceImpl<SubwareMapper, Subware> implements SubwareService {
    @Autowired
    private SubwareMapper subwareMapper;

    @Override
    public String validateSubware(Subware subware) {
        if (subware == null) return "子站信息为空";
        //首先需要检查子站信息，不允许存在同城市子站，不允许多个子站指定统一库管员
        String city = subware.getCity();
        //不允许同城市创建
        List<Subware> subwareList = this.list(new QueryWrapper<Subware>().eq("city", city).ne("id", subware.getId()));
        if (subwareList != null && subwareList.size() > 0) return "同城市已经存在子站";
        return null;
    }

    @Override
    public List<Long> getAllSubwareManager() {
        return subwareMapper.getAllSubwareManager();
    }

    @Override
    public Subware getByManagerId(Long userId) {
        //先查分库id，再反过来查分库信息
        Long subwareId = subwareMapper.getSubwareIdByManagerId(userId);
        return subwareMapper.selectById(subwareId);
    }

    @Override
    public List<Long> getSubwareMatsers(Long subwareId) {
        return subwareMapper.getSubwareMatsers(subwareId);
    }

    @Override
    public Integer addMasters(Long id, List<Long> managerIds) {
        int count = 0;
        for (Long managerId : managerIds) {
            count += subwareMapper.addMaster(id, managerId);
        }
        return count;
    }

    @Override
    public void removeMasters(Long id) {
        subwareMapper.removeMasters(id);
    }
}
