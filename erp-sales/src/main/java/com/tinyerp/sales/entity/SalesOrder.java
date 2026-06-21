package com.tinyerp.sales.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_sales_order")
public class SalesOrder extends BaseEntity {

    private String orderNo;
    private Long customerId;
    private String customerName;
    private LocalDate orderDate;
    private LocalDate expectDeliverDate;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal receivableAmount;
    private String status;
    private String remark;
}