package com.ecommerce.backend.dto.response;

import com.ecommerce.backend.enums.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    private String sku;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer minStockLevel;
    private BigDecimal weight;
    private String dimensions;
    private ProductStatus status;
    private Boolean isFeatured;
    private Boolean isDigital;
    private Boolean requiresShipping;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private CategoryResponse category;
    private List<ProductImageResponse> images;
    private List<ProductVariantResponse> variants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Calculated fields
    private Boolean isInStock;
    private Boolean isLowStock;
    private Boolean isAvailable;
    private BigDecimal discountAmount;
    private BigDecimal discountPercentage;
    
    @Data
    public static class CategoryResponse {
        private Long id;
        private String name;
        private String slug;
        private String imageUrl;
    }
    
    @Data
    public static class ProductImageResponse {
        private Long id;
        private String imageUrl;
        private String altText;
        private Boolean isPrimary;
        private Integer sortOrder;
    }
    
    @Data
    public static class ProductVariantResponse {
        private Long id;
        private String name;
        private String sku;
        private BigDecimal price;
        private BigDecimal comparePrice;
        private Integer stockQuantity;
        private String imageUrl;
        private Boolean isActive;
        private String attribute1Name;
        private String attribute1Value;
        private String attribute2Name;
        private String attribute2Value;
        private String attribute3Name;
        private String attribute3Value;
        private Boolean isInStock;
        private Boolean isAvailable;
        private String displayName;
    }
}