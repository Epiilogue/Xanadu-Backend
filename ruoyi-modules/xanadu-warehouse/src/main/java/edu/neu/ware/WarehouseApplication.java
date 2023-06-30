package edu.neu.ware;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"edu.neu","com.ruoyi.common.security.handler","edu.neu.base.config"})
@EnableFeignClients(basePackages = {"edu.neu","com.ruoyi"})
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableCaching
public class WarehouseApplication {
        public static void main(String[] args) {
            org.springframework.boot.SpringApplication.run(WarehouseApplication.class, args);
        }
}
