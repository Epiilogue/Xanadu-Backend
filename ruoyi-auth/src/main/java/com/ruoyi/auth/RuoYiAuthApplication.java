package com.ruoyi.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;

/**
 * 认证授权中心
 * 
 * @author ruoyi
 */
@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RuoYiAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiAuthApplication.class, args);
        System.out.println("      Xanadu认证授权中心启动成功   \n" +
                "\n" +
                "   _  __                      __     \n" +
                "  | |/ /___ _____  ____ _____/ /_  __\n" +
                "  |   / __ `/ __ \\/ __ `/ __  / / / /\n" +
                " /   / /_/ / / / / /_/ / /_/ / /_/ / \n" +
                "/_/|_\\__,_/_/ /_/\\__,_/\\__,_/\\__,_/  \n" +
                "                                     \n");
    }
}
