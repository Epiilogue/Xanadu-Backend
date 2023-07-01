package edu.neu.ware.service;

import edu.neu.ware.entity.Subware;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
public interface SubwareService extends IService<Subware> {


    String validateSubware(Subware subware);

    List<Long> getAllSubwareManager();

    Subware getByManagerId(Long userId);

    List<Long> getSubwareMatsers(Long subwareId);

    Integer addMasters(Long id, List<Long> managerIds);

    void removeMasters(Long id);
}
