package com.ecommerce.backend.mapper;

import com.ecommerce.backend.dto.request.OrderRequest;
import com.ecommerce.backend.dto.response.OrderResponse;
import com.ecommerce.backend.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "paymentTransactionId", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "shippedAt", ignore = true)
    @Mapping(target = "deliveredAt", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "orderItems", source = "orderItems", qualifiedByName = "mapOrderItems")
    public abstract Order toEntity(OrderRequest request);

    @Mapping(target = "canBeCancelled", expression = "java(order.canBeCancelled())")
    @Mapping(target = "isPaid", expression = "java(order.isPaid())")
    @Mapping(target = "isCompleted", expression = "java(order.isCompleted())")
    @Mapping(target = "shippingFullName", expression = "java(order.getShippingFullName())")
    @Mapping(target = "billingFullName", expression = "java(order.getBillingFullName())")
    @Mapping(target = "shippingFullAddress", expression = "java(order.getShippingFullAddress())")
    public abstract OrderResponse toResponse(Order order);

    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    public abstract OrderResponse.UserResponse toUserResponse(User user);

    public abstract OrderResponse.OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "sku", source = "product.sku")
    @Mapping(target = "slug", source = "product.slug")
    public abstract OrderResponse.OrderItemResponse.ProductResponse toOrderItemProductResponse(Product product);

    @Mapping(target = "displayName", expression = "java(variant.getDisplayName())")
    public abstract OrderResponse.OrderItemResponse.ProductVariantResponse toOrderItemVariantResponse(ProductVariant variant);

    @Named("mapOrderItems")
    protected java.util.List<OrderItem> mapOrderItems(java.util.List<OrderRequest.OrderItemRequest> orderItemRequests) {
        if (orderItemRequests == null) {
            return null;
        }
        
        return orderItemRequests.stream()
                .map(this::mapOrderItem)
                .collect(java.util.stream.Collectors.toList());
    }

    private OrderItem mapOrderItem(OrderRequest.OrderItemRequest request) {
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
}