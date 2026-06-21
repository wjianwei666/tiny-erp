package com.tinyerp.sales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyerp.sales.entity.SalesOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrder> {
}