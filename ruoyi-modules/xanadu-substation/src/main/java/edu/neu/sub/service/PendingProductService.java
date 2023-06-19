package edu.neu.sub.service;

import edu.neu.sub.entity.PendingProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.neu.sub.vo.ProductVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-19 11:12:41
 */
public interface PendingProductService extends IService<PendingProduct> {

    boolean convertAndSave(Long receiptId, List<ProductVo> products, String completed, String taskType,Long subwareId);

}
