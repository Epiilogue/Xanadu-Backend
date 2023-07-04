package edu.neu.dbc.serviceImpl;

import com.alibaba.fastjson.JSON;
import edu.neu.dbc.entity.Product;
import edu.neu.dbc.mapper.ProductMapper;
import edu.neu.dbc.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private static final RequestOptions COMMON_OPTIONS;
    private static final String PRODUCT_INDEX = "xanadu_product";

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加索引
     */
    public Boolean insertNewProdyct(Product product) {
        IndexRequest indexRequest = new IndexRequest(PRODUCT_INDEX);
        indexRequest.id(product.getId().toString());
        indexRequest.source(JSON.toJSONString(product), XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest, COMMON_OPTIONS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除索引
     */
    public Boolean deleteProduct(Long id) {
        DeleteRequest deleteRequest = new DeleteRequest(PRODUCT_INDEX);
        deleteRequest.id(id.toString());
        try {
            restHighLevelClient.delete(deleteRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更新索引
     */
    public Boolean updateProduct(Product product) {
        UpdateRequest updateRequest = new UpdateRequest(PRODUCT_INDEX, product.getId().toString());
        updateRequest.doc(JSON.toJSONString(product), XContentType.JSON);
        try {
            restHighLevelClient.update(updateRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据关键字查找,可根据商品名和商品描述查找
     */
    public List<Product> queryProducts(String content) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("name", content)).query(QueryBuilders.matchQuery("comment", content));
        SearchRequest searchRequest = new SearchRequest(new String[]{PRODUCT_INDEX}, sourceBuilder);
        System.out.println(searchRequest);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, COMMON_OPTIONS);
        SearchHit[] hits = searchResponse.getHits().getHits();
        ArrayList<Product> products = new ArrayList<>();
        for (SearchHit hit : hits) {
            Product product = JSON.parseObject(hit.getSourceAsString(), Product.class);
            products.add(product);
        }
        return products;
    }


}
