package edu.neu.ware.serviceImpl;

import edu.neu.ware.entity.CenterInput;
import edu.neu.ware.entity.CenterStorageRecord;
import edu.neu.ware.mapper.CenterStorageRecordMapper;
import edu.neu.ware.service.CenterStorageRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-04 05:08:31
 */
@Service
public class CenterStorageRecordServiceImpl extends ServiceImpl<CenterStorageRecordMapper, CenterStorageRecord> implements CenterStorageRecordService {


    @Autowired
    CenterStorageRecordMapper centerStorageRecordMapper;

    @Override
    public CenterStorageRecord createProduct(CenterInput centerInput) {
        //中心仓库不存在该商品，需要创建记录并初始化
        CenterStorageRecord centerStorageRecord = new CenterStorageRecord();
        centerStorageRecord.setProductId(centerInput.getProductId());
        centerStorageRecord.setProductName(centerInput.getProductName());
        centerStorageRecord.setProductPrice(centerInput.getProductPrice());
        centerStorageRecord.setCreateTime(new Date());
        centerStorageRecord.setUpdateTime(new Date());
        int insert = centerStorageRecordMapper.insert(centerStorageRecord);
        if (insert == 0) return null;
        return centerStorageRecord;
    }
}
