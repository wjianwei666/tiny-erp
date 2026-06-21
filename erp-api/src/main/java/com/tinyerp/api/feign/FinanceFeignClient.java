package com.tinyerp.api.feign;

import com.tinyerp.common.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "erp-finance", path = "/api/finance")
public interface FinanceFeignClient {

    @PostMapping("/receivable/create")
    Result<Void> createReceivable(@RequestParam("orderId") Long orderId,
                                  @RequestParam("customerId") Long customerId,
                                  @RequestParam("amount") BigDecimal amount);

    @PostMapping("/payable/create")
    Result<Void> createPayable(@RequestParam("orderId") Long orderId,
                               @RequestParam("supplierId") Long supplierId,
                               @RequestParam("amount") BigDecimal amount);
}