package edu.neu.ware.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Override
    public void createProduct(String name, Long productId, Double price) {
        if (name == null || productId == null || price == null) return;
        //创建商品
        //注意幂等性，先查一下有没有相同id的商品
        QueryWrapper<CenterStorageRecord> queryWrapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord record = this.getOne(queryWrapper);
        if (record != null) return;//已经存在该商品，不需要创建
        CenterStorageRecord centerStorageRecord = new CenterStorageRecord();
        centerStorageRecord.setProductId(productId);
        centerStorageRecord.setProductName(name);
        centerStorageRecord.setProductPrice(price);
        centerStorageRecord.setCreateTime(new Date());
        centerStorageRecord.setUpdateTime(new Date());
        centerStorageRecordMapper.insert(centerStorageRecord);
    }

    @Override
    public void updateProduct(String name, Long productId, Double price) {
        //更新商品
        if (name == null || productId == null || price == null) return;
        //注意幂等性，先查一下有没有相同id的商品
        QueryWrapper<CenterStorageRecord> queryWrapper = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord record = this.getOne(queryWrapper);
        if (record == null) {
            this.createProduct(name, productId, price);//不存在该商品，需要创建
            return;
        }
        record.setProductName(name);
        record.setProductPrice(price);
        record.setUpdateTime(new Date());
        this.updateById(record);
    }

    @Override
    public void deleteProduct(Long productId) {
        //注意幂等性，先查一下有没有相同id的商品
        QueryWrapper<CenterStorageRecord> record = new QueryWrapper<CenterStorageRecord>().eq("product_id", productId);
        CenterStorageRecord centerStorageRecord = this.getOne(record);
        if (centerStorageRecord == null) return;//不存在该商品，不需要删除
        this.removeById(centerStorageRecord.getId());
    }


}
