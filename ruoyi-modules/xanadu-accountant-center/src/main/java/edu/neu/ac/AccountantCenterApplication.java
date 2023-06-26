package edu.neu.ac;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 86131
 */
@SpringBootApplication(scanBasePackages = {"edu.neu", "com.ruoyi.common.security.handler"})
@EnableFeignClients(basePackages = {"edu.neu.ac.feign"})
@MapperScan("edu.neu.ac.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
public class AccountantCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountantCenterApplication.class, args);
    }


}
