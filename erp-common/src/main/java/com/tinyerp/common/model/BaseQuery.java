package com.tinyerp.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseQuery implements Serializable {
    private String keyword;
    private String startDate;
    private String endDate;
    private String orderBy;
    private String orderDirection;
}