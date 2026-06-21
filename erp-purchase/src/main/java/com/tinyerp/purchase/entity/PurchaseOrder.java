package com.tinyerp.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_purchase_order")
public class PurchaseOrder extends BaseEntity {

    private String orderNo;
    private Long supplierId;
    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectArriveDate;
    private BigDecimal totalAmount;
    private String status;
    private String remark;
}