package com.tinyerp.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyerp.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_product")
public class Product extends BaseEntity {

    private String productCode;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private String unit;
    private String spec;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer minStock;
    private Integer maxStock;
    private String imageUrl;
    private Integer status;
    private String remark;
}