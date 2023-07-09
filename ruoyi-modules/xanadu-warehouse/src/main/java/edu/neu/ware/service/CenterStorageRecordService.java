package edu.neu.ware.service;

import edu.neu.ware.entity.CenterInput;
import edu.neu.ware.entity.CenterStorageRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
public interface CenterStorageRecordService extends IService<CenterStorageRecord> {

    CenterStorageRecord createProduct(CenterInput productId);

    void createProduct(String name,Long productId,Double price);

    void updateProduct(String name,Long productId,Double price);

    void deleteProduct(Long productId);

}
