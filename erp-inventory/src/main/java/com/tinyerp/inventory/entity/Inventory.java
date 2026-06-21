package com.tinyerp.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_inventory")
public class Inventory implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long productId;
    private String productCode;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private BigDecimal quantity;
    private BigDecimal lockedQuantity;
    private BigDecimal availableQuantity;
    private BigDecimal totalCost;
    private BigDecimal lastPurchasePrice;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}