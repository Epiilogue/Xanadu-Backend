package edu.neu.ware;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"edu.neu","com.ruoyi.common.security.handler","edu.neu.base.config"})
@EnableFeignClients
@EnableDiscoveryClient
public class WarehouseApplication {
        public static void main(String[] args) {
            org.springframework.boot.SpringApplication.run(WarehouseApplication.class, args);
        }
}
