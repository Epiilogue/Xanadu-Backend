package edu.neu.dbc.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(){
           return new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("39.106.145.84",9200,"http")
                    )
            );
    }


}
