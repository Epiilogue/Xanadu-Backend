package edu.neu.sub;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"edu.neu", "com.ruoyi"})
@EnableFeignClients(basePackages = {"edu.neu.sub.feign","com.ruoyi.system.api"})
@MapperScan("edu.neu.sub.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
public class SubstationApplication {
    public static void main(String[] args) {
        //启动Spring
        org.springframework.boot.SpringApplication.run(SubstationApplication.class, args);
    }
}
