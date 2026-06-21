package com.tinyerp.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_supplier")
public class Supplier extends BaseEntity {

    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String address;
    private String bankName;
    private String bankAccount;
    private String taxNumber;
    private Integer status;
    private String remark;
}