package com.tinyerp.api.feign;

import com.tinyerp.common.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "erp-inventory", path = "/api/inventory")
public interface InventoryFeignClient {

    @GetMapping("/stock/check")
    Result<Integer> checkStock(@RequestParam("productId") Long productId);

    @GetMapping("/stock/quantity")
    Result<Integer> getStockQuantity(@RequestParam("productId") Long productId);
}