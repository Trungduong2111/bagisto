package com.ecommerce.backend.dto.response;

import com.ecommerce.backend.enums.OrderStatus;
import com.ecommerce.backend.enums.PaymentMethod;
import com.ecommerce.backend.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private UserResponse user;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentTransactionId;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private String notes;
    
    // Shipping Address
    private String shippingFirstName;
    private String shippingLastName;
    private String shippingCompany;
    private String shippingAddressLine1;
    private String shippingAddressLine2;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;
    private String shippingPhone;
    
    // Billing Address
    private String billingFirstName;
    private String billingLastName;
    private String billingCompany;
    private String billingAddressLine1;
    private String billingAddressLine2;
    private String billingCity;
    private String billingState;
    private String billingPostalCode;
    private String billingCountry;
    private String billingPhone;
    
    private List<OrderItemResponse> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Calculated fields
    private Boolean canBeCancelled;
    private Boolean isPaid;
    private Boolean isCompleted;
    private String shippingFullName;
    private String billingFullName;
    private String shippingFullAddress;
    
    @Data
    public static class UserResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String fullName;
    }
    
    @Data
    public static class OrderItemResponse {
        private Long id;
        private ProductResponse product;
        private ProductVariantResponse productVariant;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private String productName;
        private String productSku;
        private String productImageUrl;
        private String variantName;
        
        @Data
        public static class ProductResponse {
            private Long id;
            private String name;
            private String sku;
            private String slug;
        }
        
        @Data
        public static class ProductVariantResponse {
            private Long id;
            private String name;
            private String sku;
            private String displayName;
        }
    }
}