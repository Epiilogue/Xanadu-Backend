package edu.neu.dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DistributionCenterApplication {
        public static void main(String[] args) {
           SpringApplication.run(DistributionCenterApplication.class, args);
        }
}
