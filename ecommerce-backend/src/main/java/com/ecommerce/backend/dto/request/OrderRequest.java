package com.ecommerce.backend.dto.request;

import com.ecommerce.backend.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    
    @Valid
    @NotEmpty(message = "Order items are required")
    private List<OrderItemRequest> orderItems;
    
    @DecimalMin(value = "0.0", message = "Tax amount must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Tax amount format is invalid")
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Shipping amount must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Shipping amount format is invalid")
    private BigDecimal shippingAmount = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Discount amount must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Discount amount format is invalid")
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    private PaymentMethod paymentMethod;
    
    private String notes;
    
    // Shipping Address
    @NotBlank(message = "Shipping first name is required")
    private String shippingFirstName;
    
    @NotBlank(message = "Shipping last name is required")
    private String shippingLastName;
    
    private String shippingCompany;
    
    @NotBlank(message = "Shipping address line 1 is required")
    private String shippingAddressLine1;
    
    private String shippingAddressLine2;
    
    @NotBlank(message = "Shipping city is required")
    private String shippingCity;
    
    private String shippingState;
    
    @NotBlank(message = "Shipping postal code is required")
    private String shippingPostalCode;
    
    @NotBlank(message = "Shipping country is required")
    private String shippingCountry;
    
    private String shippingPhone;
    
    // Billing Address
    @NotBlank(message = "Billing first name is required")
    private String billingFirstName;
    
    @NotBlank(message = "Billing last name is required")
    private String billingLastName;
    
    private String billingCompany;
    
    @NotBlank(message = "Billing address line 1 is required")
    private String billingAddressLine1;
    
    private String billingAddressLine2;
    
    @NotBlank(message = "Billing city is required")
    private String billingCity;
    
    private String billingState;
    
    @NotBlank(message = "Billing postal code is required")
    private String billingPostalCode;
    
    @NotBlank(message = "Billing country is required")
    private String billingCountry;
    
    private String billingPhone;
    
    @Data
    public static class OrderItemRequest {
        
        @NotNull(message = "Product ID is required")
        private Long productId;
        
        private Long productVariantId;
        
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}