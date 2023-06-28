package com.ruoyi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RuoYiGatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiGatewayApplication.class, args);
        System.out.println("      Xanadu网关启动成功   \n" +
                "\n" +
                "   _  __                      __     \n" +
                "  | |/ /___ _____  ____ _____/ /_  __\n" +
                "  |   / __ `/ __ \\/ __ `/ __  / / / /\n" +
                " /   / /_/ / / / / /_/ / /_/ / /_/ / \n" +
                "/_/|_\\__,_/_/ /_/\\__,_/\\__,_/\\__,_/  \n" +
                "                                     \n");
    }
}
