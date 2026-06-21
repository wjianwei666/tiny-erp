package com.tinyerp.purchase.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.common.model.Result;
import com.tinyerp.purchase.entity.PurchaseOrder;
import com.tinyerp.purchase.entity.PurchaseOrderItem;
import com.tinyerp.purchase.entity.PurchaseReceipt;
import com.tinyerp.purchase.entity.Supplier;
import com.tinyerp.purchase.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "采购管理", description = "供应商、采购订单、入库单管理")
@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Operation(summary = "供应商分页列表")
    @GetMapping("/supplier/page")
    public Result<Page<Supplier>> pageSupplier(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(purchaseService.pageSupplier(pageNum, pageSize, keyword));
    }

    @Operation(summary = "获取供应商详情")
    @GetMapping("/supplier/{id}")
    public Result<Supplier> getSupplier(@PathVariable Long id) {
        return Result.success(purchaseService.getSupplier(id));
    }

    @Operation(summary = "新增/更新供应商")
    @PostMapping("/supplier")
    public Result<Void> saveSupplier(@RequestBody Supplier supplier) {
        purchaseService.saveSupplier(supplier);
        return Result.success();
    }

    @Operation(summary = "删除供应商")
    @DeleteMapping("/supplier/{id}")
    public Result<Void> deleteSupplier(@PathVariable Long id) {
        purchaseService.deleteSupplier(id);
        return Result.success();
    }

    @Operation(summary = "采购订单分页列表")
    @GetMapping("/order/page")
    public Result<Page<PurchaseOrder>> pageOrder(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        return Result.success(purchaseService.pageOrder(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "获取采购订单详情")
    @GetMapping("/order/{id}")
    public Result<PurchaseOrder> getOrder(@PathVariable Long id) {
        return Result.success(purchaseService.getOrder(id));
    }

    @Operation(summary = "获取采购订单明细")
    @GetMapping("/order/{orderId}/items")
    public Result<List<PurchaseOrderItem>> getOrderItems(@PathVariable Long orderId) {
        return Result.success(purchaseService.getOrderItems(orderId));
    }

    @Operation(summary = "新增/更新采购订单")
    @PostMapping("/order")
    public Result<Void> saveOrder(@RequestBody PurchaseOrder order,
                                  @RequestParam(required = false) List<PurchaseOrderItem> items) {
        purchaseService.saveOrder(order, items);
        return Result.success();
    }

    @Operation(summary = "删除采购订单")
    @DeleteMapping("/order/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        purchaseService.deleteOrder(id);
        return Result.success();
    }

    @Operation(summary = "入库单分页列表")
    @GetMapping("/receipt/page")
    public Result<Page<PurchaseReceipt>> pageReceipt(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(purchaseService.pageReceipt(pageNum, pageSize, keyword));
    }
}