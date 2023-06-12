package edu.neu.dpc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"edu.neu"})
@EnableFeignClients(basePackages = {"edu.neu"})
@MapperScan("edu.neu.dpc.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
public class DispatchCenterApplication{
    public static void main(String[] args) {
        //启动spring
        org.springframework.boot.SpringApplication.run(DispatchCenterApplication.class, args);
    }
}