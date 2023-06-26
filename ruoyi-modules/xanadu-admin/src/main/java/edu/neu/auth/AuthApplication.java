package edu.neu.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author jin Zhang
 */
@SpringBootApplication
@EnableFeignClients
public class AuthApplication {
        public static void main(String[] args) {
            org.springframework.boot.SpringApplication.run(AuthApplication.class, args);
        }
}
