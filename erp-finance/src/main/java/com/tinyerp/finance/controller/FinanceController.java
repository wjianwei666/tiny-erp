package com.tinyerp.finance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.common.model.Result;
import com.tinyerp.finance.entity.Payable;
import com.tinyerp.finance.entity.Receivable;
import com.tinyerp.finance.entity.Transaction;
import com.tinyerp.finance.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Tag(name = "财务管理", description = "应收、应付、收支流水管理")
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @Operation(summary = "应收分页列表")
    @GetMapping("/receivable/page")
    public Result<Page<Receivable>> pageReceivable(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return Result.success(financeService.pageReceivable(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "创建应收记录")
    @PostMapping("/receivable/create")
    public Result<Void> createReceivable(@RequestParam Long orderId,
                                         @RequestParam Long customerId,
                                         @RequestParam BigDecimal amount) {
        financeService.createReceivable(orderId, customerId, amount);
        return Result.success();
    }

    @Operation(summary = "应付分页列表")
    @GetMapping("/payable/page")
    public Result<Page<Payable>> pagePayable(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return Result.success(financeService.pagePayable(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "创建应付记录")
    @PostMapping("/payable/create")
    public Result<Void> createPayable(@RequestParam Long orderId,
                                      @RequestParam Long supplierId,
                                      @RequestParam BigDecimal amount) {
        financeService.createPayable(orderId, supplierId, amount);
        return Result.success();
    }

    @Operation(summary = "收支记录分页")
    @GetMapping("/transaction/page")
    public Result<Page<Transaction>> pageTransaction(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String type) {
        return Result.success(financeService.pageTransaction(pageNum, pageSize, type));
    }

    @Operation(summary = "新增收支记录")
    @PostMapping("/transaction")
    public Result<Void> addTransaction(@RequestBody Transaction transaction) {
        financeService.addTransaction(transaction);
        return Result.success();
    }

    @Operation(summary = "财务看板数据")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return Result.success(financeService.getDashboard());
    }
}