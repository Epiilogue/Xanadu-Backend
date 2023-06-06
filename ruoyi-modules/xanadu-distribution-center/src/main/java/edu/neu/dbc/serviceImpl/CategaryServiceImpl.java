package edu.neu.dbc.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.neu.dbc.entity.Categary;
import edu.neu.dbc.mapper.CategaryMapper;
import edu.neu.dbc.service.CategaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@Service
public class CategaryServiceImpl extends ServiceImpl<CategaryMapper, Categary> implements CategaryService {

    @Autowired
    CategaryMapper categaryMapper;

    @Override
    public List<Categary> getChildrenCategary(Integer id) {
        return this.list(new QueryWrapper<Categary>().eq("parent_id", id));
    }

    @Override
    public List<Categary> listTree() {
        List<Categary> list = this.list();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.stream().filter(categary -> categary.getLevel() == 1).peek(categary -> {
            categary.setChildren(this.getChildrenCategary(categary.getId()));
        }).collect(Collectors.toList());
    }
}
