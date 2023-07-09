package edu.neu.ware.listener;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import edu.neu.base.constant.cc.MQTopic;
import edu.neu.ware.service.CenterStorageRecordService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RocketMQMessageListener(topic = "product-topic", consumerGroup = "ware-canal-group")
public class ProductListener implements RocketMQListener<String> {

    @Autowired
    CenterStorageRecordService centerStorageRecordService;


    @Getter
    @Setter
    @TableName("dbc_product")
    @ApiModel(value = "Product对象", description = "")
    public static class Product implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String name;
        private Double price;
    }


    @Override
    public void onMessage(String msg) {
        if (!JSONUtil.isJson(msg)) {
            return;
        }
        System.out.println(msg);
        JSONObject msgJsonObject = JSON.parseObject(msg);
        //检查一下改变的表的信息来源，如果不是product表就不用管了
        String tableName = msgJsonObject.getString("table");
        if (!tableName.equals("dbc_product")) return;
        //拿到发生改变的数据信息
        JSONArray jsonArray = msgJsonObject.getJSONArray("data");
        //遍历数据
        List<Product> products = new ArrayList<>();
        for (Object o : jsonArray) {
            products.add(JSON.parseObject(JSON.toJSONString(o), Product.class));
        }
        String sqlType = msgJsonObject.getString("type");
        switch (sqlType) {
            case "UPDATE":
                for (Product product : products)
                    centerStorageRecordService.updateProduct(product.getName(),product.getId(),product.getPrice());
                break;
            case "INSERT":
                for (Product product : products)
                    centerStorageRecordService.createProduct(product.getName(),product.getId(),product.getPrice());
                break;
            case "DELETE":
                for (Product product : products)
                    centerStorageRecordService.deleteProduct(product.getId());
                break;
            default:
                System.out.println("不同步的消息类型：" + sqlType);
                return;
        }
    }
}