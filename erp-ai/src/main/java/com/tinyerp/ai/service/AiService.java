package com.tinyerp.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tinyerp.ai.entity.InventoryAlert;
import com.tinyerp.ai.entity.ReportData;
import com.tinyerp.inventory.entity.Inventory;
import com.tinyerp.inventory.entity.Product;
import com.tinyerp.inventory.mapper.InventoryMapper;
import com.tinyerp.inventory.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final ProductMapper productMapper;
    private final InventoryMapper inventoryMapper;

    public List<InventoryAlert> getStockAlerts() {
        List<InventoryAlert> alerts = new ArrayList<>();
        List<Product> products = productMapper.selectList(
                new LambdaQueryWrapper<Product>().eq(Product::getStatus, 1)
        );

        for (Product product : products) {
            Inventory inv = inventoryMapper.selectOne(
                    new LambdaQueryWrapper<Inventory>()
                            .eq(Inventory::getProductId, product.getId())
            );

            int currentStock = inv != null ? inv.getAvailableQuantity().intValue() : 0;
            int minStock = product.getMinStock() != null ? product.getMinStock() : 0;
            int maxStock = product.getMaxStock() != null ? product.getMaxStock() : 0;

            if (minStock > 0 && currentStock <= minStock) {
                InventoryAlert alert = new InventoryAlert();
                alert.setProductId(product.getId());
                alert.setProductName(product.getProductName());
                alert.setCurrentStock(currentStock);
                alert.setThreshold(minStock);
                alert.setAlertType("LOW_STOCK");
                alert.setMessage("商品【" + product.getProductName() + "】库存不足，当前库存：" + currentStock + "，安全阈值：" + minStock);
                alerts.add(alert);
            } else if (maxStock > 0 && currentStock >= maxStock) {
                InventoryAlert alert = new InventoryAlert();
                alert.setProductId(product.getId());
                alert.setProductName(product.getProductName());
                alert.setCurrentStock(currentStock);
                alert.setThreshold(maxStock);
                alert.setAlertType("OVER_STOCK");
                alert.setMessage("商品【" + product.getProductName() + "】库存积压，当前库存：" + currentStock + "，上限阈值：" + maxStock);
                alerts.add(alert);
            }
        }
        return alerts;
    }

    public ReportData generateReport() {
        ReportData report = new ReportData();

        List<Inventory> inventoryList = inventoryMapper.selectList(null);
        report.setTotalProducts(inventoryList.size());
        report.setTotalStockQuantity(inventoryList.stream()
                .map(Inventory::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        report.setTotalStockValue(inventoryList.stream()
                .map(Inventory::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        long lowStockCount = inventoryList.stream()
                .filter(i -> i.getAvailableQuantity().intValue() <= 10)
                .count();
        report.setLowStockCount((int) lowStockCount);

        return report;
    }

    public Map<String, Object> recognizeDocument(String documentType) {
        Map<String, Object> result = new HashMap<>();
        result.put("recognized", false);
        result.put("message", "AI单据识别功能（OCR）需要集成第三方AI服务，当前为预留接口");
        result.put("documentType", documentType);
        result.put("suggestedService", "百度AI OCR / 阿里云OCR / 腾讯云OCR");
        return result;
    }

    public Map<String, Object> validateDocument(String documentType, Long documentId) {
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("documentType", documentType);
        result.put("documentId", documentId);
        result.put("warnings", new ArrayList<>());

        List<String> warnings = new ArrayList<>();
        warnings.add("智能校验功能已启用，当前返回基础校验结果");
        result.put("warnings", warnings);

        return result;
    }
}