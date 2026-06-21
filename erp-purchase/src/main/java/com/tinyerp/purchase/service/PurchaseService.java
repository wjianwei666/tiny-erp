package com.tinyerp.purchase.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyerp.purchase.entity.*;
import com.tinyerp.purchase.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final SupplierMapper supplierMapper;
    private final PurchaseOrderMapper orderMapper;
    private final PurchaseOrderItemMapper orderItemMapper;
    private final PurchaseReceiptMapper receiptMapper;

    public Page<Supplier> pageSupplier(long pageNum, long pageSize, String keyword) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<Supplier>()
                .like(StrUtil.isNotBlank(keyword), Supplier::getSupplierName, keyword)
                .orderByDesc(Supplier::getCreateTime);
        return supplierMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public Supplier getSupplier(Long id) {
        return supplierMapper.selectById(id);
    }

    public void saveSupplier(Supplier supplier) {
        if (supplier.getId() == null) {
            supplier.setSupplierCode("SUP" + DateUtil.format(LocalDate.now(), "yyyyMMdd") + IdWorker.getId());
            supplierMapper.insert(supplier);
        } else {
            supplierMapper.updateById(supplier);
        }
    }

    public void deleteSupplier(Long id) {
        supplierMapper.deleteById(id);
    }

    public Page<PurchaseOrder> pageOrder(long pageNum, long pageSize, String keyword, String status) {
        LambdaQueryWrapper<PurchaseOrder> wrapper = new LambdaQueryWrapper<PurchaseOrder>()
                .like(StrUtil.isNotBlank(keyword), PurchaseOrder::getOrderNo, keyword)
                .eq(StrUtil.isNotBlank(status), PurchaseOrder::getStatus, status)
                .orderByDesc(PurchaseOrder::getCreateTime);
        return orderMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    public PurchaseOrder getOrder(Long id) {
        PurchaseOrder order = orderMapper.selectById(id);
        if (order != null) {
            List<PurchaseOrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<PurchaseOrderItem>()
                            .eq(PurchaseOrderItem::getOrderId, id)
            );
            order.setRemark(null);
        }
        return order;
    }

    public List<PurchaseOrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.selectList(
                new LambdaQueryWrapper<PurchaseOrderItem>()
                        .eq(PurchaseOrderItem::getOrderId, orderId)
                        .orderByAsc(PurchaseOrderItem::getSortOrder)
        );
    }

    @Transactional
    public void saveOrder(PurchaseOrder order, List<PurchaseOrderItem> items) {
        if (order.getId() == null) {
            order.setOrderNo("PO" + DateUtil.format(LocalDate.now(), "yyyyMMdd") + IdWorker.getId());
            order.setStatus("DRAFT");
            orderMapper.insert(order);
        } else {
            orderMapper.updateById(order);
            orderItemMapper.delete(new LambdaQueryWrapper<PurchaseOrderItem>()
                    .eq(PurchaseOrderItem::getOrderId, order.getId()));
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i = 0; i < items.size(); i++) {
            PurchaseOrderItem item = items.get(i);
            item.setOrderId(order.getId());
            item.setSortOrder(i);
            if (item.getTotalPrice() == null) {
                item.setTotalPrice(item.getUnitPrice().multiply(item.getQuantity()));
            }
            totalAmount = totalAmount.add(item.getTotalPrice());
            orderItemMapper.insert(item);
        }

        order.setTotalAmount(totalAmount);
        orderMapper.updateById(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderMapper.deleteById(id);
        orderItemMapper.delete(new LambdaQueryWrapper<PurchaseOrderItem>()
                .eq(PurchaseOrderItem::getOrderId, id));
    }

    public Page<PurchaseReceipt> pageReceipt(long pageNum, long pageSize, String keyword) {
        LambdaQueryWrapper<PurchaseReceipt> wrapper = new LambdaQueryWrapper<PurchaseReceipt>()
                .like(StrUtil.isNotBlank(keyword), PurchaseReceipt::getReceiptNo, keyword)
                .orderByDesc(PurchaseReceipt::getCreateTime);
        return receiptMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }
}