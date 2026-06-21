package com.tinyerp.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("fin_transaction")
public class Transaction extends BaseEntity {

    private String transactionNo;
    private String transactionType;
    private BigDecimal amount;
    private String relatedType;
    private Long relatedId;
    private String relatedNo;
    private String counterpartyName;
    private String paymentMethod;
    private LocalDate transactionDate;
    private String remark;
}