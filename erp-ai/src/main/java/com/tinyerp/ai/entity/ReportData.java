package com.tinyerp.ai.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ReportData implements Serializable {

    private int totalProducts;
    private BigDecimal totalStockQuantity;
    private BigDecimal totalStockValue;
    private int lowStockCount;
    private BigDecimal totalReceivable;
    private BigDecimal totalPayable;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}