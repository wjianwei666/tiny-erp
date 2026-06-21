package com.tinyerp.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fin_payable")
public class Payable extends BaseEntity {

    private Long supplierId;
    private String supplierName;
    private Long orderId;
    private String orderNo;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;
    private LocalDate dueDate;
    private String status;
    private String remark;
}