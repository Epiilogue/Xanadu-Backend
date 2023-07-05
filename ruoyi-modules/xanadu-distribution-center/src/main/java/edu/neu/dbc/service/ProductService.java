package edu.neu.dbc.service;

import com.alibaba.fastjson.JSON;
import edu.neu.dbc.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
public interface ProductService extends IService<Product> {
    public Boolean insertNewProdyct(Product product);

    /**
     * 删除索引
     */
    public Boolean deleteProduct(Long id);

    /**
     * 更新索引
     */
    public Boolean updateProduct(Product product);

    /**
     * 根据关键字查找,可根据商品名和商品描述查找
     */
    public List<Product> queryProducts(String content) throws IOException;
}
