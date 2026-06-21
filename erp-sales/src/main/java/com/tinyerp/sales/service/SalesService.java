package com.tinyerp.sales.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.sales.entity.*;
import com.tinyerp.sales.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final CustomerMapper customerMapper;
    private final SalesOrderMapper orderMapper;
    private final SalesOrderItemMapper orderItemMapper;
    private final SalesDeliveryMapper deliveryMapper;

    public Page<Customer> pageCustomer(long pageNum, long pageSize, String keyword) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<Customer>()
                .like(StrUtil.isNotBlank(keyword), Customer::getCustomerName, keyword)
                .orderByDesc(Customer::getCreateTime);
        return customerMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public Customer getCustomer(Long id) {
        return customerMapper.selectById(id);
    }

    public void saveCustomer(Customer customer) {
        if (customer.getId() == null) {
            customer.setCustomerCode("CUS" + DateUtil.format(LocalDate.now(), "yyyyMMdd") + IdWorker.getId());
            customerMapper.insert(customer);
        } else {
            customerMapper.updateById(customer);
        }
    }

    public void deleteCustomer(Long id) {
        customerMapper.deleteById(id);
    }

    public Page<SalesOrder> pageOrder(long pageNum, long pageSize, String keyword, String status) {
        LambdaQueryWrapper<SalesOrder> wrapper = new LambdaQueryWrapper<SalesOrder>()
                .like(StrUtil.isNotBlank(keyword), SalesOrder::getOrderNo, keyword)
                .eq(StrUtil.isNotBlank(status), SalesOrder::getStatus, status)
                .orderByDesc(SalesOrder::getCreateTime);
        return orderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public SalesOrder getOrder(Long id) {
        return orderMapper.selectById(id);
    }

    public List<SalesOrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.selectList(
                new LambdaQueryWrapper<SalesOrderItem>()
                        .eq(SalesOrderItem::getOrderId, orderId)
                        .orderByAsc(SalesOrderItem::getSortOrder)
        );
    }

    @Transactional
    public void saveOrder(SalesOrder order, List<SalesOrderItem> items) {
        if (order.getId() == null) {
            order.setOrderNo("SO" + DateUtil.format(LocalDate.now(), "yyyyMMdd") + IdWorker.getId());
            order.setStatus("DRAFT");
            orderMapper.insert(order);
        } else {
            orderMapper.updateById(order);
            orderItemMapper.delete(new LambdaQueryWrapper<SalesOrderItem>()
                    .eq(SalesOrderItem::getOrderId, order.getId()));
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i = 0; i < items.size(); i++) {
            SalesOrderItem item = items.get(i);
            item.setOrderId(order.getId());
            item.setSortOrder(i);
            if (item.getTotalPrice() == null) {
                item.setTotalPrice(item.getUnitPrice().multiply(item.getQuantity()));
            }
            totalAmount = totalAmount.add(item.getTotalPrice());
            orderItemMapper.insert(item);
        }

        order.setTotalAmount(totalAmount);
        order.setReceivableAmount(totalAmount.subtract(
                order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO));
        orderMapper.updateById(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderMapper.deleteById(id);
        orderItemMapper.delete(new LambdaQueryWrapper<SalesOrderItem>()
                .eq(SalesOrderItem::getOrderId, id));
    }

    public Page<SalesDelivery> pageDelivery(long pageNum, long pageSize, String keyword) {
        LambdaQueryWrapper<SalesDelivery> wrapper = new LambdaQueryWrapper<SalesDelivery>()
                .like(StrUtil.isNotBlank(keyword), SalesDelivery::getDeliveryNo, keyword)
                .orderByDesc(SalesDelivery::getCreateTime);
        return deliveryMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }
}