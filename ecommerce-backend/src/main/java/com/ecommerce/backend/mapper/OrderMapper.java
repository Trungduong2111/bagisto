package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.dto.response.OrderResponse;
import com.ecommerce.backend.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequest request) {
        if (request == null) {
            return null;
        }

        Order order = new Order();
        
        // Map basic fields
        order.setTaxAmount(request.getTaxAmount());
        order.setShippingAmount(request.getShippingAmount());
        order.setDiscountAmount(request.getDiscountAmount());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNotes(request.getNotes());
        
        // Map shipping address
        order.setShippingFirstName(request.getShippingFirstName());
        order.setShippingLastName(request.getShippingLastName());
        order.setShippingCompany(request.getShippingCompany());
        order.setShippingAddressLine1(request.getShippingAddressLine1());
        order.setShippingAddressLine2(request.getShippingAddressLine2());
        order.setShippingCity(request.getShippingCity());
        order.setShippingState(request.getShippingState());
        order.setShippingPostalCode(request.getShippingPostalCode());
        order.setShippingCountry(request.getShippingCountry());
        order.setShippingPhone(request.getShippingPhone());
        
        // Map billing address
        order.setBillingFirstName(request.getBillingFirstName());
        order.setBillingLastName(request.getBillingLastName());
        order.setBillingCompany(request.getBillingCompany());
        order.setBillingAddressLine1(request.getBillingAddressLine1());
        order.setBillingAddressLine2(request.getBillingAddressLine2());
        order.setBillingCity(request.getBillingCity());
        order.setBillingState(request.getBillingState());
        order.setBillingPostalCode(request.getBillingPostalCode());
        order.setBillingCountry(request.getBillingCountry());
        order.setBillingPhone(request.getBillingPhone());
        
        return order;
    }

    public Order mapOrderWithItems(OrderRequest request) {
        Order order = toEntity(request);
        
        if (request.getOrderItems() != null) {
            List<OrderItem> orderItems = request.getOrderItems().stream()
                    .map(this::mapOrderItem)
                    .collect(Collectors.toList());
            order.setOrderItems(orderItems);
        }
        
        return order;
    }

    public OrderItem mapOrderItem(OrderRequest.OrderItemRequest request) {
        if (request == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        
        // Set product
        Product product = new Product();
        product.setId(request.getProductId());
        orderItem.setProduct(product);
        
        // Set product variant if provided
        if (request.getProductVariantId() != null) {
            ProductVariant variant = new ProductVariant();
            variant.setId(request.getProductVariantId());
            orderItem.setProductVariant(variant);
        }
        
        orderItem.setQuantity(request.getQuantity());
        
        return orderItem;
    }

    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponse response = new OrderResponse();
        
        // Map basic fields
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus());
        response.setSubtotal(order.getSubtotal());
        response.setTaxAmount(order.getTaxAmount());
        response.setShippingAmount(order.getShippingAmount());
        response.setDiscountAmount(order.getDiscountAmount());
        response.setTotalAmount(order.getTotalAmount());
        response.setCurrency(order.getCurrency());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setPaymentTransactionId(order.getPaymentTransactionId());
        response.setPaidAt(order.getPaidAt());
        response.setShippedAt(order.getShippedAt());
        response.setDeliveredAt(order.getDeliveredAt());
        response.setCancelledAt(order.getCancelledAt());
        response.setCancellationReason(order.getCancellationReason());
        response.setNotes(order.getNotes());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        
        // Map shipping address
        response.setShippingFirstName(order.getShippingFirstName());
        response.setShippingLastName(order.getShippingLastName());
        response.setShippingCompany(order.getShippingCompany());
        response.setShippingAddressLine1(order.getShippingAddressLine1());
        response.setShippingAddressLine2(order.getShippingAddressLine2());
        response.setShippingCity(order.getShippingCity());
        response.setShippingState(order.getShippingState());
        response.setShippingPostalCode(order.getShippingPostalCode());
        response.setShippingCountry(order.getShippingCountry());
        response.setShippingPhone(order.getShippingPhone());
        
        // Map billing address
        response.setBillingFirstName(order.getBillingFirstName());
        response.setBillingLastName(order.getBillingLastName());
        response.setBillingCompany(order.getBillingCompany());
        response.setBillingAddressLine1(order.getBillingAddressLine1());
        response.setBillingAddressLine2(order.getBillingAddressLine2());
        response.setBillingCity(order.getBillingCity());
        response.setBillingState(order.getBillingState());
        response.setBillingPostalCode(order.getBillingPostalCode());
        response.setBillingCountry(order.getBillingCountry());
        response.setBillingPhone(order.getBillingPhone());
        
        // Map calculated fields
        response.setCanBeCancelled(order.canBeCancelled());
        response.setIsPaid(order.isPaid());
        response.setIsCompleted(order.isCompleted());
        response.setShippingFullName(order.getShippingFullName());
        response.setBillingFullName(order.getBillingFullName());
        response.setShippingFullAddress(order.getShippingFullAddress());
        
        // Map user
        if (order.getUser() != null) {
            response.setUser(toUserResponse(order.getUser()));
        }
        
        // Map order items
        if (order.getOrderItems() != null) {
            response.setOrderItems(order.getOrderItems().stream()
                    .map(this::toOrderItemResponse)
                    .collect(Collectors.toList()));
        }
        
        return response;
    }

    public OrderResponse.UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        OrderResponse.UserResponse response = new OrderResponse.UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setFullName(user.getFullName());
        
        return response;
    }

    public OrderResponse.OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderResponse.OrderItemResponse response = new OrderResponse.OrderItemResponse();
        response.setId(orderItem.getId());
        response.setQuantity(orderItem.getQuantity());
        response.setUnitPrice(orderItem.getUnitPrice());
        response.setTotalPrice(orderItem.getTotalPrice());
        response.setProductName(orderItem.getProductName());
        response.setProductSku(orderItem.getProductSku());
        response.setProductImageUrl(orderItem.getProductImageUrl());
        response.setVariantName(orderItem.getVariantName());
        
        // Map product
        if (orderItem.getProduct() != null) {
            response.setProduct(toOrderItemProductResponse(orderItem.getProduct()));
        }
        
        // Map product variant
        if (orderItem.getProductVariant() != null) {
            response.setProductVariant(toOrderItemVariantResponse(orderItem.getProductVariant()));
        }
        
        return response;
    }

    public OrderResponse.OrderItemResponse.ProductResponse toOrderItemProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        OrderResponse.OrderItemResponse.ProductResponse response = new OrderResponse.OrderItemResponse.ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setSlug(product.getSlug());
        
        return response;
    }

    public OrderResponse.OrderItemResponse.ProductVariantResponse toOrderItemVariantResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }

        OrderResponse.OrderItemResponse.ProductVariantResponse response = new OrderResponse.OrderItemResponse.ProductVariantResponse();
        response.setId(variant.getId());
        response.setName(variant.getName());
        response.setSku(variant.getSku());
        response.setDisplayName(variant.getDisplayName());
        
        return response;
    }
}