package com.tinyerp.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.tinyerp.ai", "com.tinyerp.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.tinyerp.api.feign")
public class ErpAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpAiApplication.class, args);
    }
}