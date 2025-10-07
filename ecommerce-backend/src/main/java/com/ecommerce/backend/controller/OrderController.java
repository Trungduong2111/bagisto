package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.dto.response.OrderResponse;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.enums.OrderStatus;
import com.ecommerce.backend.enums.PaymentStatus;
import com.ecommerce.backend.mapper.OrderMapper;
import com.ecommerce.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get all orders (Admin only)")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.getAllOrders(pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Get current user's orders")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.getOrdersByUser(user, pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        Order order = orderService.getOrderById(id);
        
        // Check if user can access this order
        if (!user.getRole().name().startsWith("ADMIN") && !order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number")
    public ResponseEntity<OrderResponse> getOrderByNumber(
            @PathVariable String orderNumber,
            @AuthenticationPrincipal User user) {
        Order order = orderService.getOrderByNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Check if user can access this order
        if (!user.getRole().name().startsWith("ADMIN") && !order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get orders by status (Admin only)")
    public ResponseEntity<Page<OrderResponse>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.getOrdersByStatus(status, pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            @AuthenticationPrincipal User user) {
        Order order = orderMapper.mapOrderWithItems(request);
        order.setUser(user);
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.ok(orderMapper.toResponse(savedOrder));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Update order status (Admin only)")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(orderMapper.toResponse(updatedOrder));
    }

    @PatchMapping("/{id}/payment-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Update payment status (Admin only)")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus paymentStatus,
            @RequestParam(required = false) String transactionId) {
        Order updatedOrder = orderService.updatePaymentStatus(id, paymentStatus, transactionId);
        return ResponseEntity.ok(orderMapper.toResponse(updatedOrder));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal User user) {
        Order order = orderService.getOrderById(id);
        
        // Check if user can cancel this order
        if (!user.getRole().name().startsWith("ADMIN") && !order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        
        Order cancelledOrder = orderService.cancelOrder(id, reason);
        return ResponseEntity.ok(orderMapper.toResponse(cancelledOrder));
    }

    @PatchMapping("/{id}/notes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Add note to order (Admin only)")
    public ResponseEntity<OrderResponse> addOrderNote(
            @PathVariable Long id,
            @RequestParam String note) {
        Order updatedOrder = orderService.addOrderNote(id, note);
        return ResponseEntity.ok(orderMapper.toResponse(updatedOrder));
    }
}