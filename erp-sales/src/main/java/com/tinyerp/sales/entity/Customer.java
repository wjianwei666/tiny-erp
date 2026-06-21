package com.tinyerp.sales.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_customer")
public class Customer extends BaseEntity {

    private String customerCode;
    private String customerName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String address;
    private String customerType;
    private BigDecimal creditLimit;
    private Integer status;
    private String remark;
}