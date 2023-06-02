package edu.neu.dbc.service;

import edu.neu.dbc.entity.Categary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
public interface CategaryService extends IService<Categary> {

    List<Categary> getChildrenCategary(Integer id);

    List<Categary> listTree();
}
