package edu.neu.invoice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author jin Zhang
 */
@SpringBootApplication
@EnableFeignClients
public class InvoiceApplication {
        public static void main(String[] args) {
            org.springframework.boot.SpringApplication.run(InvoiceApplication.class, args);
        }
}
