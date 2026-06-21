package com.tinyerp.inventory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.common.model.Result;
import com.tinyerp.inventory.entity.*;
import com.tinyerp.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Tag(name = "库存管理", description = "商品档案、库存、盘点管理")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "商品分页列表")
    @GetMapping("/product/page")
    public Result<Page<Product>> pageProduct(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId) {
        return Result.success(inventoryService.pageProduct(pageNum, pageSize, keyword, categoryId));
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/product/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        return Result.success(inventoryService.getProduct(id));
    }

    @Operation(summary = "新增/更新商品")
    @PostMapping("/product")
    public Result<Void> saveProduct(@RequestBody Product product) {
        inventoryService.saveProduct(product);
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/product/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        inventoryService.deleteProduct(id);
        return Result.success();
    }

    @Operation(summary = "库存分页列表")
    @GetMapping("/stock/page")
    public Result<Page<Inventory>> pageInventory(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(inventoryService.pageInventory(pageNum, pageSize, keyword));
    }

    @Operation(summary = "获取商品库存")
    @GetMapping("/stock/{productId}")
    public Result<Inventory> getStock(@PathVariable Long productId) {
        return Result.success(inventoryService.getInventory(productId));
    }

    @Operation(summary = "查询库存数量")
    @GetMapping("/stock/quantity")
    public Result<Integer> getStockQuantity(@RequestParam Long productId) {
        Inventory inv = inventoryService.getInventory(productId);
        return Result.success(inv != null ? inv.getAvailableQuantity().intValue() : 0);
    }

    @Operation(summary = "入库操作")
    @PostMapping("/stock/in")
    public Result<Void> stockIn(@RequestBody Map<String, Object> params) {
        Long productId = Long.valueOf(params.get("productId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        BigDecimal unitPrice = params.get("unitPrice") != null ?
                new BigDecimal(params.get("unitPrice").toString()) : null;
        String referenceNo = (String) params.get("referenceNo");
        inventoryService.stockIn(productId, quantity, unitPrice, referenceNo);
        return Result.success();
    }

    @Operation(summary = "出库操作")
    @PostMapping("/stock/out")
    public Result<Void> stockOut(@RequestBody Map<String, Object> params) {
        Long productId = Long.valueOf(params.get("productId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String referenceNo = (String) params.get("referenceNo");
        inventoryService.stockOut(productId, quantity, referenceNo);
        return Result.success();
    }

    @Operation(summary = "库存变动记录")
    @GetMapping("/log/page")
    public Result<Page<InventoryLog>> pageLog(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) Long productId) {
        return Result.success(inventoryService.pageLog(pageNum, pageSize, productId));
    }

    @Operation(summary = "盘点单列表")
    @GetMapping("/check/page")
    public Result<Page<StockCheck>> pageCheck(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return Result.success(inventoryService.pageCheck(pageNum, pageSize));
    }
}