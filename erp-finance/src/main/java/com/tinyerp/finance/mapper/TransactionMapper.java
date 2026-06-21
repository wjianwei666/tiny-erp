package com.tinyerp.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyerp.finance.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
}