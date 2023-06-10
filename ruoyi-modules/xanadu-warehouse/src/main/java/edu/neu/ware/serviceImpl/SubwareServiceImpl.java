package edu.neu.ware.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.neu.ware.entity.Subware;
import edu.neu.ware.mapper.SubwareMapper;
import edu.neu.ware.service.SubwareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public String validateSubware(Subware subware) {
        if (subware == null) return "子站信息为空";
        //首先需要检查子站信息，不允许存在同城市子站，不允许多个子站指定统一库管员
        String city = subware.getCity();
        //不允许同城市创建
        List<Subware> subwareList = this.list(new QueryWrapper<Subware>().eq("city", city).ne("id", subware.getId()));
        if (subwareList != null && subwareList.size() > 0) return "同城市已经存在子站";
        //不允许多个子站指定统一库管员
        Long master = subware.getMaster();
        List<Subware> masterList = this.list(new QueryWrapper<Subware>().eq("master", master).ne("id", subware.getId()));
        if (masterList != null && masterList.size() > 0) return "同一库管员已经存在子站";

        return null;
    }
}
