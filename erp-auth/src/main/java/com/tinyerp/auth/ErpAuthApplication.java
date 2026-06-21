package com.tinyerp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.tinyerp.auth", "com.tinyerp.common"})
@EnableDiscoveryClient
public class ErpAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpAuthApplication.class, args);
    }
}