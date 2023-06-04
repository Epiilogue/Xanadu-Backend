package edu.neu.cc.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class UnSubscribeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对应的order表记录ID")
    private Long id;

    @ApiModelProperty("操作订单ID")
    private Long orderId;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("操作类型(退货，换货，退订)")
    private Integer operationType;

    @ApiModelProperty("退换货状态")
    private String status;

    // key: product id, value: numbers
    Map<Long, Integer> productsMap;


    public Double getTotalAmount(Map<Long, Double> productIdPriceMap) {
        double total = 0;
        for (Map.Entry<Long, Integer> entry : productsMap.entrySet()) {
            Long productId = entry.getKey();
            Integer numbers = entry.getValue();
            Double price = productIdPriceMap.get(productId);
            total += price * numbers;
        }
        return total;
    }

    public Integer getTotalNumbers(Map<Long, Integer> productIdNumberMap) {
        int total = 0;
        for (Map.Entry<Long, Integer> entry : productsMap.entrySet()) {
            Integer numbers = entry.getValue();
            total += numbers;
        }
        return total;
    }
}
