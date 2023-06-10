package edu.neu.dpc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"edu.neu"})
@EnableFeignClients(basePackages = {"edu.neu"})
@MapperScan("edu.neu.dpc.mapper")
@EnableTransactionManagement
public class DispatchCenterApplication{
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}