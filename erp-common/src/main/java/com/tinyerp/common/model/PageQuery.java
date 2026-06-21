package com.tinyerp.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageQuery extends BaseQuery {

    private long pageNum = 1;
    private long pageSize = 10;
}