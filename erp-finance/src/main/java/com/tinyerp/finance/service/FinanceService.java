package com.tinyerp.finance.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.finance.entity.*;
import com.tinyerp.finance.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final ReceivableMapper receivableMapper;
    private final PayableMapper payableMapper;
    private final TransactionMapper transactionMapper;

    public Page<Receivable> pageReceivable(long pageNum, long pageSize, String keyword, String status) {
        LambdaQueryWrapper<Receivable> wrapper = new LambdaQueryWrapper<Receivable>()
                .like(StrUtil.isNotBlank(keyword), Receivable::getCustomerName, keyword)
                .eq(StrUtil.isNotBlank(status), Receivable::getStatus, status)
                .orderByDesc(Receivable::getCreateTime);
        return receivableMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Transactional
    public void createReceivable(Long orderId, Long customerId, BigDecimal amount) {
        Receivable receivable = new Receivable();
        receivable.setCustomerId(customerId);
        receivable.setOrderId(orderId);
        receivable.setAmount(amount);
        receivable.setReceivedAmount(BigDecimal.ZERO);
        receivable.setBalanceAmount(amount);
        receivable.setDueDate(LocalDate.now().plusDays(30));
        receivable.setStatus("PENDING");
        receivableMapper.insert(receivable);
    }

    public Page<Payable> pagePayable(long pageNum, long pageSize, String keyword, String status) {
        LambdaQueryWrapper<Payable> wrapper = new LambdaQueryWrapper<Payable>()
                .like(StrUtil.isNotBlank(keyword), Payable::getSupplierName, keyword)
                .eq(StrUtil.isNotBlank(status), Payable::getStatus, status)
                .orderByDesc(Payable::getCreateTime);
        return payableMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Transactional
    public void createPayable(Long orderId, Long supplierId, BigDecimal amount) {
        Payable payable = new Payable();
        payable.setSupplierId(supplierId);
        payable.setOrderId(orderId);
        payable.setAmount(amount);
        payable.setPaidAmount(BigDecimal.ZERO);
        payable.setBalanceAmount(amount);
        payable.setDueDate(LocalDate.now().plusDays(30));
        payable.setStatus("PENDING");
        payableMapper.insert(payable);
    }

    public Page<Transaction> pageTransaction(long pageNum, long pageSize, String type) {
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<Transaction>()
                .eq(StrUtil.isNotBlank(type), Transaction::getTransactionType, type)
                .orderByDesc(Transaction::getTransactionDate);
        return transactionMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Transactional
    public void addTransaction(Transaction transaction) {
        transaction.setTransactionNo("TXN" + DateUtil.format(LocalDate.now(), "yyyyMMdd") + IdWorker.getId());
        transactionMapper.insert(transaction);
    }

    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        BigDecimal totalReceivable = receivableMapper.selectList(null).stream()
                .filter(r -> !"PAID".equals(r.getStatus()))
                .map(Receivable::getBalanceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dashboard.put("totalReceivable", totalReceivable);

        BigDecimal totalPayable = payableMapper.selectList(null).stream()
                .filter(p -> !"PAID".equals(p.getStatus()))
                .map(Payable::getBalanceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dashboard.put("totalPayable", totalPayable);

        BigDecimal totalIncome = transactionMapper.selectList(
                new LambdaQueryWrapper<Transaction>().eq(Transaction::getTransactionType, "INCOME")
        ).stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        dashboard.put("totalIncome", totalIncome);

        BigDecimal totalExpense = transactionMapper.selectList(
                new LambdaQueryWrapper<Transaction>().eq(Transaction::getTransactionType, "EXPENSE")
        ).stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        dashboard.put("totalExpense", totalExpense);

        return dashboard;
    }
}