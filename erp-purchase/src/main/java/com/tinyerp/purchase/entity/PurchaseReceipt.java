package com.tinyerp.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_purchase_receipt")
public class PurchaseReceipt extends BaseEntity {

    private String receiptNo;
    private Long orderId;
    private String orderNo;
    private Long supplierId;
    private String supplierName;
    private LocalDate receiptDate;
    private BigDecimal totalAmount;
    private String status;
    private String remark;
}