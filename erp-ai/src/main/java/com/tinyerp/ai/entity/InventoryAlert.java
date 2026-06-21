package com.tinyerp.ai.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class InventoryAlert implements Serializable {

    private Long productId;
    private String productName;
    private int currentStock;
    private int threshold;
    private String alertType;
    private String message;
}