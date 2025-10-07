package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.enums.OrderStatus;
import com.ecommerce.backend.enums.PaymentStatus;
import com.ecommerce.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        // Generate order number
        String orderNumber = generateOrderNumber();
        order.setOrderNumber(orderNumber);
        
        // Calculate totals
        calculateOrderTotals(order);
        
        Order savedOrder = orderRepository.save(order);
        log.info("Created new order with number: {}", orderNumber);
        return savedOrder;
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        OrderStatus previousStatus = order.getStatus();
        order.setStatus(status);
        
        // Update timestamps based on status
        LocalDateTime now = LocalDateTime.now();
        switch (status) {
            case SHIPPED:
                if (order.getShippedAt() == null) {
                    order.setShippedAt(now);
                }
                break;
            case DELIVERED:
                if (order.getDeliveredAt() == null) {
                    order.setDeliveredAt(now);
                }
                break;
            case CANCELLED:
                if (order.getCancelledAt() == null) {
                    order.setCancelledAt(now);
                }
                break;
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order {} status from {} to {}", order.getOrderNumber(), previousStatus, status);
        return updatedOrder;
    }

    public Order updatePaymentStatus(Long id, PaymentStatus paymentStatus, String transactionId) {
        Order order = getOrderById(id);
        PaymentStatus previousStatus = order.getPaymentStatus();
        order.setPaymentStatus(paymentStatus);
        
        if (transactionId != null) {
            order.setPaymentTransactionId(transactionId);
        }
        
        if (paymentStatus == PaymentStatus.PAID && order.getPaidAt() == null) {
            order.setPaidAt(LocalDateTime.now());
            // Auto-confirm order when payment is successful
            if (order.getStatus() == OrderStatus.PENDING) {
                order.setStatus(OrderStatus.CONFIRMED);
            }
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order {} payment status from {} to {}", order.getOrderNumber(), previousStatus, paymentStatus);
        return updatedOrder;
    }

    public Order cancelOrder(Long id, String reason) {
        Order order = getOrderById(id);
        
        if (!order.canBeCancelled()) {
            throw new RuntimeException("Order cannot be cancelled in current status: " + order.getStatus());
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(reason);
        
        Order cancelledOrder = orderRepository.save(order);
        log.info("Cancelled order {} with reason: {}", order.getOrderNumber(), reason);
        return cancelledOrder;
    }

    public Order addOrderNote(Long id, String note) {
        Order order = getOrderById(id);
        String existingNotes = order.getNotes();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String newNote = "[" + timestamp + "] " + note;
        
        if (existingNotes != null && !existingNotes.trim().isEmpty()) {
            order.setNotes(existingNotes + "\n" + newNote);
        } else {
            order.setNotes(newNote);
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Added note to order {}", order.getOrderNumber());
        return updatedOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .filter(order -> !order.isDeleted())
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Transactional(readOnly = true)
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findByDeletedAtIsNull(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersByUser(User user, Pageable pageable) {
        return orderRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> getOrdersByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable) {
        return orderRepository.findByPaymentStatus(paymentStatus, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> searchOrders(String search, Pageable pageable) {
        return orderRepository.findBySearchTerm(search, pageable);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = orderRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = orderRepository.getRevenueByDateRange(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public long getTotalOrdersCount() {
        return orderRepository.countActiveOrders();
    }

    @Transactional(readOnly = true)
    public long getOrdersCountByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long getOrdersCountByUser(User user) {
        return orderRepository.countByUser(user);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = String.valueOf((int) (Math.random() * 1000));
        String orderNumber = "ORD" + timestamp + randomSuffix;
        
        // Ensure uniqueness
        while (orderRepository.existsByOrderNumber(orderNumber)) {
            randomSuffix = String.valueOf((int) (Math.random() * 1000));
            orderNumber = "ORD" + timestamp + randomSuffix;
        }
        
        return orderNumber;
    }

    private void calculateOrderTotals(Order order) {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order must have at least one item");
        }
        
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            item.setTotalPrice(item.calculateTotalPrice());
            subtotal = subtotal.add(item.getTotalPrice());
        }
        
        order.setSubtotal(subtotal);
        
        // Calculate total: subtotal + tax + shipping - discount
        BigDecimal total = subtotal;
        if (order.getTaxAmount() != null) {
            total = total.add(order.getTaxAmount());
        }
        if (order.getShippingAmount() != null) {
            total = total.add(order.getShippingAmount());
        }
        if (order.getDiscountAmount() != null) {
            total = total.subtract(order.getDiscountAmount());
        }
        
        order.setTotalAmount(total);
    }
}