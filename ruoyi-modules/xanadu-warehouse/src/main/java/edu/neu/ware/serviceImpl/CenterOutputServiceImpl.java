package edu.neu.ware.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.neu.ware.entity.CenterOutput;
import edu.neu.ware.mapper.CenterOutputMapper;
import edu.neu.ware.service.CenterOutputService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
@Service
public class CenterOutputServiceImpl extends ServiceImpl<CenterOutputMapper, CenterOutput> implements CenterOutputService {

    /**
     * 此方法输入任务ID，给出任务ID
     */
    @Override
    public Integer checkTaskStatus(Long taskId, String status) {
        QueryWrapper<CenterOutput> eq = new QueryWrapper<CenterOutput>().eq("task_id", taskId).eq("status", status);
        return Math.toIntExact(this.count(eq));
    }
}
