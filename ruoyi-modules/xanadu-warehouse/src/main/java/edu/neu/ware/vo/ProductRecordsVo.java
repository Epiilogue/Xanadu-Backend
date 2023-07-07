package edu.neu.ware.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据传输，对应每一个商品是否有缺货
 */
@Data
public class ProductRecordsVo implements Serializable {

    // 商品ID和缺货数量的键值对
    Map<Long, Integer> productIdNumberMap=new HashMap<>();

    // 是否缺货
    Boolean isLack;

    public void addProductRecord(Long productId, Integer number) {
        productIdNumberMap.put(productId, number);
    }


}
