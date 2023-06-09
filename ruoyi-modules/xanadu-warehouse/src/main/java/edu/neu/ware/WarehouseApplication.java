package edu.neu.ware;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"edu.neu"})
@EnableFeignClients
public class WarehouseApplication {
        public static void main(String[] args) {
            org.springframework.boot.SpringApplication.run(WarehouseApplication.class, args);
        }
}
