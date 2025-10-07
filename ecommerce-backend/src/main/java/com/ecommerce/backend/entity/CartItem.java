package com.ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public BigDecimal getTotalPrice() {
        BigDecimal unitPrice = productVariant != null ? productVariant.getPrice() : product.getPrice();
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public String getDisplayName() {
        if (productVariant != null) {
            return product.getName() + " - " + productVariant.getName();
        }
        return product.getName();
    }
}