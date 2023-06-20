package edu.neu.sub.service;

import edu.neu.sub.entity.ReceiptProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.neu.sub.vo.ProductVo;

import java.util.List;

/**
 * <p>
 * 订单商品的签收情况 服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
public interface ReceiptProductService extends IService<ReceiptProduct> {

    List<ReceiptProduct> convertAndSave(Long receiptId, List<ProductVo> products, String completed,String taskType);
}
