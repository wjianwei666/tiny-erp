package com.tinyerp.sales.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.common.model.Result;
import com.tinyerp.sales.entity.Customer;
import com.tinyerp.sales.entity.SalesDelivery;
import com.tinyerp.sales.entity.SalesOrder;
import com.tinyerp.sales.entity.SalesOrderItem;
import com.tinyerp.sales.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "销售管理", description = "客户、销售订单、出库单管理")
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @Operation(summary = "客户分页列表")
    @GetMapping("/customer/page")
    public Result<Page<Customer>> pageCustomer(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(salesService.pageCustomer(pageNum, pageSize, keyword));
    }

    @Operation(summary = "获取客户详情")
    @GetMapping("/customer/{id}")
    public Result<Customer> getCustomer(@PathVariable Long id) {
        return Result.success(salesService.getCustomer(id));
    }

    @Operation(summary = "新增/更新客户")
    @PostMapping("/customer")
    public Result<Void> saveCustomer(@RequestBody Customer customer) {
        salesService.saveCustomer(customer);
        return Result.success();
    }

    @Operation(summary = "删除客户")
    @DeleteMapping("/customer/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        salesService.deleteCustomer(id);
        return Result.success();
    }

    @Operation(summary = "销售订单分页列表")
    @GetMapping("/order/page")
    public Result<Page<SalesOrder>> pageOrder(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return Result.success(salesService.pageOrder(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "获取销售订单详情")
    @GetMapping("/order/{id}")
    public Result<SalesOrder> getOrder(@PathVariable Long id) {
        return Result.success(salesService.getOrder(id));
    }

    @Operation(summary = "获取销售订单明细")
    @GetMapping("/order/{orderId}/items")
    public Result<List<SalesOrderItem>> getOrderItems(@PathVariable Long orderId) {
        return Result.success(salesService.getOrderItems(orderId));
    }

    @Operation(summary = "新增/更新销售订单")
    @PostMapping("/order")
    public Result<Void> saveOrder(@RequestBody SalesOrder order,
                                  @RequestParam(required = false) List<SalesOrderItem> items) {
        salesService.saveOrder(order, items);
        return Result.success();
    }

    @Operation(summary = "删除销售订单")
    @DeleteMapping("/order/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        salesService.deleteOrder(id);
        return Result.success();
    }

    @Operation(summary = "出库单分页列表")
    @GetMapping("/delivery/page")
    public Result<Page<SalesDelivery>> pageDelivery(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(salesService.pageDelivery(pageNum, pageSize, keyword));
    }
}