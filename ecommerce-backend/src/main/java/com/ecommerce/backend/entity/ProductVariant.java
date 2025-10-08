package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "compare_price", precision = 10, scale = 2)
    private BigDecimal comparePrice;

    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "weight", precision = 8, scale = 2)
    private BigDecimal weight;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Variant attributes (e.g., color, size)
    @Column(name = "attribute_1_name")
    private String attribute1Name;

    @Column(name = "attribute_1_value")
    private String attribute1Value;

    @Column(name = "attribute_2_name")
    private String attribute2Name;

    @Column(name = "attribute_2_value")
    private String attribute2Value;

    @Column(name = "attribute_3_name")
    private String attribute3Name;

    @Column(name = "attribute_3_value")
    private String attribute3Value;

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public boolean isAvailable() {
        return isActive && isInStock();
    }

    public BigDecimal getEffectivePrice() {
        return price != null ? price : product.getPrice();
    }

    public String getDisplayName() {
        StringBuilder displayName = new StringBuilder(name);
        
        if (attribute1Name != null && attribute1Value != null) {
            displayName.append(" - ").append(attribute1Name).append(": ").append(attribute1Value);
        }
        
        if (attribute2Name != null && attribute2Value != null) {
            displayName.append(" - ").append(attribute2Name).append(": ").append(attribute2Value);
        }
        
        if (attribute3Name != null && attribute3Value != null) {
            displayName.append(" - ").append(attribute3Name).append(": ").append(attribute3Value);
        }
        
        return displayName.toString();
    }
}