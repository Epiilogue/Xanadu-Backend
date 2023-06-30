package edu.neu.ac;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {UtilAutoConfiguration.class}, scanBasePackages = {"edu.neu", "com.ruoyi.common.security.handler"})
@EnableFeignClients(basePackages = {"edu.neu.ac.feign"})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableCaching
public class AccountantCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountantCenterApplication.class, args);
    }


}
