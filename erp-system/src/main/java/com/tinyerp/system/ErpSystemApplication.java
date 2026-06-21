package com.tinyerp.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.tinyerp.system", "com.tinyerp.common"})
@EnableDiscoveryClient
public class ErpSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpSystemApplication.class, args);
    }
}