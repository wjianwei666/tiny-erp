package com.tinyerp.inventory.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.common.exception.BusinessException;
import com.tinyerp.inventory.entity.*;
import com.tinyerp.inventory.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductMapper productMapper;
    private final InventoryMapper inventoryMapper;
    private final InventoryLogMapper inventoryLogMapper;
    private final StockCheckMapper stockCheckMapper;

    public Page<Product> pageProduct(long pageNum, long pageSize, String keyword, Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .like(StrUtil.isNotBlank(keyword), Product::getProductName, keyword)
                .eq(categoryId != null, Product::getCategoryId, categoryId)
                .orderByDesc(Product::getCreateTime);
        return productMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public Product getProduct(Long id) {
        return productMapper.selectById(id);
    }

    public void saveProduct(Product product) {
        if (product.getId() == null) {
            product.setProductCode("PROD" + DateUtil.format(LocalDate.now(), "yyyyMMdd") + IdWorker.getId());
            productMapper.insert(product);
        } else {
            productMapper.updateById(product);
        }
    }

    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    public Page<Inventory> pageInventory(long pageNum, long pageSize, String keyword) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<Inventory>()
                .like(StrUtil.isNotBlank(keyword), Inventory::getProductName, keyword)
                .orderByDesc(Inventory::getUpdateTime);
        return inventoryMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public Inventory getInventory(Long productId) {
        return inventoryMapper.selectOne(
                new LambdaQueryWrapper<Inventory>()
                        .eq(Inventory::getProductId, productId)
        );
    }

    @Transactional
    public void stockIn(Long productId, BigDecimal quantity, BigDecimal unitPrice, String referenceNo) {
        Inventory inventory = getOrCreateInventory(productId);
        BigDecimal beforeQty = inventory.getQuantity();
        BigDecimal afterQty = beforeQty.add(quantity);

        inventory.setQuantity(afterQty);
        inventory.setAvailableQuantity(afterQty.subtract(inventory.getLockedQuantity()));
        if (unitPrice != null) {
            inventory.setLastPurchasePrice(unitPrice);
            inventory.setTotalCost(inventory.getTotalCost().add(unitPrice.multiply(quantity)));
        }
        inventoryMapper.updateById(inventory);

        recordLog(productId, inventory.getProductName(), "PURCHASE_IN", quantity,
                beforeQty, afterQty, referenceNo, "PURCHASE_RECEIPT");
    }

    @Transactional
    public void stockOut(Long productId, BigDecimal quantity, String referenceNo) {
        Inventory inventory = getOrCreateInventory(productId);
        BigDecimal beforeQty = inventory.getQuantity();
        BigDecimal afterQty = beforeQty.subtract(quantity);

        if (afterQty.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("库存不足，当前库存：" + beforeQty);
        }

        inventory.setQuantity(afterQty);
        inventory.setAvailableQuantity(afterQty.subtract(inventory.getLockedQuantity()));
        inventoryMapper.updateById(inventory);

        recordLog(productId, inventory.getProductName(), "SALES_OUT", quantity.negate(),
                beforeQty, afterQty, referenceNo, "SALES_DELIVERY");
    }

    private Inventory getOrCreateInventory(Long productId) {
        Inventory inventory = getInventory(productId);
        if (inventory == null) {
            Product product = productMapper.selectById(productId);
            if (product == null) {
                throw new BusinessException("商品不存在");
            }
            inventory = new Inventory();
            inventory.setProductId(productId);
            inventory.setProductCode(product.getProductCode());
            inventory.setProductName(product.getProductName());
            inventory.setWarehouseId(0L);
            inventory.setWarehouseName("默认仓库");
            inventory.setQuantity(BigDecimal.ZERO);
            inventory.setLockedQuantity(BigDecimal.ZERO);
            inventory.setAvailableQuantity(BigDecimal.ZERO);
            inventory.setTotalCost(BigDecimal.ZERO);
            inventoryMapper.insert(inventory);
        }
        return inventory;
    }

    private void recordLog(Long productId, String productName, String changeType, BigDecimal changeQuantity,
                           BigDecimal beforeQuantity, BigDecimal afterQuantity, String referenceNo, String referenceType) {
        InventoryLog log = new InventoryLog();
        log.setProductId(productId);
        log.setProductName(productName);
        log.setChangeType(changeType);
        log.setChangeQuantity(changeQuantity);
        log.setBeforeQuantity(beforeQuantity);
        log.setAfterQuantity(afterQuantity);
        log.setReferenceNo(referenceNo);
        log.setReferenceType(referenceType);
        inventoryLogMapper.insert(log);
    }

    public Page<InventoryLog> pageLog(long pageNum, long pageSize, Long productId) {
        LambdaQueryWrapper<InventoryLog> wrapper = new LambdaQueryWrapper<InventoryLog>()
                .eq(productId != null, InventoryLog::getProductId, productId)
                .orderByDesc(InventoryLog::getCreateTime);
        return inventoryLogMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public Page<StockCheck> pageCheck(long pageNum, long pageSize) {
        return stockCheckMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<StockCheck>().orderByDesc(StockCheck::getCreateTime));
    }
}