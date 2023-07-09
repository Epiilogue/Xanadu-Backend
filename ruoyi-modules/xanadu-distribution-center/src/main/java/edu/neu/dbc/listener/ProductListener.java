package edu.neu.dbc.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.neu.base.constant.cc.MQTopic;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.service.ProductService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * canal 同步变化后会更新到
 */
@Component
@RocketMQMessageListener(topic = MQTopic.PRODUCT_TOPIC, consumerGroup = "product-canal-group")
public class ProductListener implements RocketMQListener<String> {

    @Autowired
    ProductService productService;

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
                    productService.updateProduct(product);
                break;
            case "INSERT":
                for (Product product : products)
                    productService.insertNewProdyct(product);
                break;
            case "DELETE":
                for (Product product : products)
                    productService.deleteProduct(product.getId());
                break;
            default:
                System.out.println("不同步的消息类型：" + sqlType);
                return;
        }
    }
}