package com.tinyerp.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_inventory_log")
public class InventoryLog implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long productId;
    private String productName;
    private String changeType;
    private BigDecimal changeQuantity;
    private BigDecimal beforeQuantity;
    private BigDecimal afterQuantity;
    private String referenceNo;
    private String referenceType;
    private String remark;
    private LocalDateTime createTime;
    private Long createBy;
}