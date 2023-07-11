package edu.neu.dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(exclude = {UtilAutoConfiguration.class}, scanBasePackages = {"edu.neu","com.ruoyi"})
@EnableFeignClients(basePackages = {"edu.neu.dbc.feign","com.ruoyi"})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableCaching
public class DistributionCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistributionCenterApplication.class, args);
    }
}
