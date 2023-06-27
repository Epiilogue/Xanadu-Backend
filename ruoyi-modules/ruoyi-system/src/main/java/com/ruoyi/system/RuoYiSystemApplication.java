package com.ruoyi.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class RuoYiSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiSystemApplication.class, args);
        System.out.println("      Xanadu系统模块启动成功   \n" +
                "\n" +
                "   _  __                      __     \n" +
                "  | |/ /___ _____  ____ _____/ /_  __\n" +
                "  |   / __ `/ __ \\/ __ `/ __  / / / /\n" +
                " /   / /_/ / / / / /_/ / /_/ / /_/ / \n" +
                "/_/|_\\__,_/_/ /_/\\__,_/\\__,_/\\__,_/  \n" +
                "                                     \n");
    }
}








