package edu.neu.ware.service;

import edu.neu.ware.entity.CenterOutput;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
public interface CenterOutputService extends IService<CenterOutput> {

    Integer checkTaskStatus(Long taskId,String status);
}
