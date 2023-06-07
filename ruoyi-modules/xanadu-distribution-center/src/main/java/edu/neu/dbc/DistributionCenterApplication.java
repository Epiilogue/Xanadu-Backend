package edu.neu.dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(exclude = {UtilAutoConfiguration.class}, scanBasePackages = {"edu.neu.*"})
@EnableFeignClients(basePackages = {"edu.neu.dbc.feign"})
@EnableTransactionManagement
public class DistributionCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistributionCenterApplication.class, args);
    }
}
