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
}
