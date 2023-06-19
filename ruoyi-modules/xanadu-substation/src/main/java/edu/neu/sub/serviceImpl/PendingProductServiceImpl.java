package edu.neu.sub.serviceImpl;

import edu.neu.base.constant.cc.ReceiptStatus;
import edu.neu.base.constant.cc.TaskType;
import edu.neu.sub.entity.PendingProduct;
import edu.neu.sub.entity.ReceiptProduct;
import edu.neu.sub.mapper.PendingProductMapper;
import edu.neu.sub.service.PendingProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.neu.sub.vo.ProductVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-19 11:12:41
 */
@Service
@Transactional
public class PendingProductServiceImpl extends ServiceImpl<PendingProductMapper, PendingProduct> implements PendingProductService {

    @Override
    public boolean convertAndSave(Long taskId, List<ProductVo> products, String completed, String taskType,Long subwareId) {
        //先转化成ReceiptProduct对象
        List<PendingProduct> pendingProducts = new ArrayList<>();
        HashMap<Long, ProductVo> longProductVoHashMap = new HashMap<>();
        products.forEach(p -> longProductVoHashMap.put(p.getProductId(), p));

        products.forEach(p -> {
            PendingProduct pendingProduct = new PendingProduct(null, taskId, p.getProductId(), p.getProductName(), p.getPrice(), p.getActualNumber(), p.getReason(),subwareId);
            pendingProducts.add(pendingProduct);
        });
        switch (completed) {
            //对于所有的情况，我们需要根据不同情况设置不同的
            case ReceiptStatus.COMPLETED:
                switch (taskType) {
                    //完成送货任务
                    case TaskType.DELIVERY:
                    case TaskType.PAYMENT_DELIVERY:
                        pendingProducts.forEach(p -> p.setDealNumber(0));
                        break;
                    //完成换货退货任务,退所有的商品数
                    case TaskType.EXCHANGE:
                    case TaskType.RETURN:
                        pendingProducts.forEach(p -> p.setDealNumber(longProductVoHashMap.get(p.getProductId()).getNumber()));
                        break;
                }
                break;
            case ReceiptStatus.FAILED:
                switch (taskType) {
                    case TaskType.DELIVERY:
                    case TaskType.PAYMENT_DELIVERY:
                        pendingProducts.forEach(p -> p.setDealNumber(longProductVoHashMap.get(p.getProductId()).getNumber()));
                        break;
                    case TaskType.EXCHANGE:
                    case TaskType.RETURN:
                        pendingProducts.forEach(p -> p.setDealNumber(0));
                        break;
                }
                break;
            //部分成功
            case ReceiptStatus.PARTIAL_COMPLETED:
                switch (taskType) {
                    case TaskType.DELIVERY:
                    case TaskType.PAYMENT_DELIVERY:
                        //所有数量减去实际数量
                        pendingProducts.forEach(p -> p.setDealNumber(longProductVoHashMap.get(p.getProductId()).getNumber() - p.getDealNumber()));
                        break;
                    case TaskType.EXCHANGE:
                    case TaskType.RETURN:
                        break;
                }
                break;
            default:
                return false;
        }
        return this.saveBatch(pendingProducts);
    }
}
