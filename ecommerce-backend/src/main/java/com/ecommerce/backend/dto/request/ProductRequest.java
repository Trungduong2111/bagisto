package com.ecommerce.backend.dto.request;

import com.ecommerce.backend.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;
    
    private String description;
    
    private String shortDescription;
    
    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Compare price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Compare price format is invalid")
    private BigDecimal comparePrice;
    
    @DecimalMin(value = "0.0", message = "Cost price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Cost price format is invalid")
    private BigDecimal costPrice;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Integer stockQuantity;
    
    @Min(value = 0, message = "Min stock level must be greater than or equal to 0")
    private Integer minStockLevel = 0;
    
    @DecimalMin(value = "0.0", message = "Weight must be greater than or equal to 0")
    @Digits(integer = 6, fraction = 2, message = "Weight format is invalid")
    private BigDecimal weight;
    
    private String dimensions;
    
    private ProductStatus status = ProductStatus.ACTIVE;
    
    private Boolean isFeatured = false;
    
    private Boolean isDigital = false;
    
    private Boolean requiresShipping = true;
    
    private String metaTitle;
    
    private String metaDescription;
    
    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must not exceed 255 characters")
    private String slug;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
}