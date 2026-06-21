package com.tinyerp.purchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.tinyerp.purchase", "com.tinyerp.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.tinyerp.api.feign")
public class ErpPurchaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpPurchaseApplication.class, args);
    }
}