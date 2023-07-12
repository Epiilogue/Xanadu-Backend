package edu.neu.sub.serviceImpl;

import com.ruoyi.common.core.domain.R;
import edu.neu.base.constant.cc.*;
import edu.neu.sub.entity.ReceiptProduct;
import edu.neu.sub.mapper.ReceiptProductMapper;
import edu.neu.sub.service.ReceiptProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.neu.sub.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单商品的签收情况 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-16 03:27:59
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReceiptProductServiceImpl extends ServiceImpl<ReceiptProductMapper, ReceiptProduct> implements ReceiptProductService {

    @Autowired
    ReceiptProductMapper receiptProductMapper;

    @Override
    public List<ReceiptProduct> convertAndSave(Long receiptId, List<ProductVo> products, String completed, String taskType) {
        //先转化成ReceiptProduct对象
        List<ReceiptProduct> receiptProducts = new ArrayList<>();
        products.forEach(p -> {
            //先不设置签收数量和退货数量，等待之后根据类型进行判断后设置,总数量不一定是签收数量+退货数量，因为换货的时候，客户那边有等量商品
            ReceiptProduct receiptProduct = new ReceiptProduct(receiptId, p.getProductId(), p.getProductName(),
                    p.getPrice(), p.getNumber(), p.getActualNumber(), 0, 0.0, 0.0);
            receiptProducts.add(receiptProduct);
        });
        switch (completed) {
            //对于所有的情况，我们需要根据不同情况设置不同的
            case ReceiptStatus.COMPLETED:
                switch (taskType) {
                    //完成送货任务
                    case TaskType.DELIVERY:
                        receiptProducts.forEach(p -> {
                            p.setSignNum(p.getAllNum());
                            p.setReturnNum(0);
                        });
                        //完成送货收款任务
                        break;
                    case TaskType.PAYMENT_DELIVERY:
                        receiptProducts.forEach(p -> {
                            p.setSignNum(p.getAllNum());
                            p.setReturnNum(0);
                            p.setInputMoney(p.getAllNum() * p.getPrice());
                        });
                        break;
                    //完成换货任务
                    case TaskType.EXCHANGE:
                        receiptProducts.forEach(p -> {
                            p.setSignNum(p.getAllNum());
                            p.setReturnNum(p.getAllNum());
                        });
                        break;
                    //完成退货任务
                    case TaskType.RETURN:
                        receiptProducts.forEach(p -> {
                            p.setSignNum(0);
                            p.setReturnNum(p.getAllNum());
                            p.setOutputMoney(p.getAllNum() * p.getPrice());
                        });
                        break;
                }
                break;
            case ReceiptStatus.FAILED:
                switch (taskType) {
                    case TaskType.DELIVERY:
                        receiptProducts.forEach(p -> {
                            p.setSignNum(0);
                            p.setReturnNum(p.getAllNum());
                            p.setOutputMoney(p.getAllNum() * p.getPrice());
                        });
                        break;
                    case TaskType.EXCHANGE:
                    case TaskType.RETURN:
                    case TaskType.PAYMENT_DELIVERY:
                        receiptProducts.forEach(p -> {
                            p.setSignNum(0);
                            p.setReturnNum(p.getAllNum());
                        });
                        break;
                }
                break;
            //部分成功
            case ReceiptStatus.PARTIAL_COMPLETED:
                switch (taskType) {
                    case TaskType.DELIVERY:
                        receiptProducts.forEach(p -> {
                            Integer actualNum = p.getSignNum();//先拿到实际数量
                            p.setSignNum(actualNum);
                            p.setReturnNum(p.getAllNum() - actualNum);
                            p.setOutputMoney(p.getReturnNum() * p.getPrice());
                        });
                    case TaskType.EXCHANGE:
                        receiptProducts.forEach(p -> {
                            Integer actualNum = p.getSignNum();//先拿到实际数量
                            p.setSignNum(actualNum);
                            p.setReturnNum(actualNum);//实际换过去几个，就退回来几个
                        });
                        break;
                    case TaskType.RETURN:
                        receiptProducts.forEach(p -> {
                                    Integer actualNum = p.getReturnNum();//先拿到实际数量
                                    p.setSignNum(0);
                                    p.setReturnNum(actualNum);//实际退回来几个，就签收几个
                                    p.setOutputMoney(actualNum * p.getPrice());
                                }
                        );
                        break;
                    case TaskType.PAYMENT_DELIVERY:
                        receiptProducts.forEach(p -> {
                            Integer actualNum = p.getSignNum();//先拿到实际数量
                            p.setSignNum(actualNum);
                            p.setReturnNum(p.getAllNum() - actualNum);
                            p.setInputMoney(actualNum * p.getPrice());
                        });
                        break;
                }
                break;
            default:
                return null;
        }
        boolean b = this.saveBatch(receiptProducts);
        if (b) {
            return receiptProducts;
        } else {
            return null;
        }
    }
}
