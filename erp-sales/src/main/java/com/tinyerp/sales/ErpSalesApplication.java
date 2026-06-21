package com.tinyerp.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.tinyerp.sales", "com.tinyerp.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.tinyerp.api.feign")
public class ErpSalesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpSalesApplication.class, args);
    }
}