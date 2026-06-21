package com.tinyerp.sales.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_sales_delivery")
public class SalesDelivery extends BaseEntity {

    private String deliveryNo;
    private Long orderId;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private LocalDate deliveryDate;
    private BigDecimal totalAmount;
    private String status;
    private String remark;
}