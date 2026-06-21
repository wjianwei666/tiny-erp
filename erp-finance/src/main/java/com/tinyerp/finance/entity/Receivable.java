package com.tinyerp.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fin_receivable")
public class Receivable extends BaseEntity {

    private Long customerId;
    private String customerName;
    private Long orderId;
    private String orderNo;
    private BigDecimal amount;
    private BigDecimal receivedAmount;
    private BigDecimal balanceAmount;
    private LocalDate dueDate;
    private String status;
    private String remark;
}