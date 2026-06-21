package com.tinyerp.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_stock_check")
public class StockCheck extends BaseEntity {

    private String checkNo;
    private LocalDate checkDate;
    private String checkType;
    private String status;
    private Integer totalProducts;
    private Integer diffProducts;
    private String remark;
}