package com.ecommerce.backend.entity;

import com.ecommerce.backend.enums.OrderStatus;
import com.ecommerce.backend.enums.PaymentMethod;
import com.ecommerce.backend.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "shipping_amount", precision = 10, scale = 2)
    private BigDecimal shippingAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false)
    private String currency = "VND";

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_transaction_id")
    private String paymentTransactionId;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Shipping Address
    @Column(name = "shipping_first_name", nullable = false)
    private String shippingFirstName;

    @Column(name = "shipping_last_name", nullable = false)
    private String shippingLastName;

    @Column(name = "shipping_company")
    private String shippingCompany;

    @Column(name = "shipping_address_line_1", nullable = false)
    private String shippingAddressLine1;

    @Column(name = "shipping_address_line_2")
    private String shippingAddressLine2;

    @Column(name = "shipping_city", nullable = false)
    private String shippingCity;

    @Column(name = "shipping_state")
    private String shippingState;

    @Column(name = "shipping_postal_code", nullable = false)
    private String shippingPostalCode;

    @Column(name = "shipping_country", nullable = false)
    private String shippingCountry;

    @Column(name = "shipping_phone")
    private String shippingPhone;

    // Billing Address
    @Column(name = "billing_first_name", nullable = false)
    private String billingFirstName;

    @Column(name = "billing_last_name", nullable = false)
    private String billingLastName;

    @Column(name = "billing_company")
    private String billingCompany;

    @Column(name = "billing_address_line_1", nullable = false)
    private String billingAddressLine1;

    @Column(name = "billing_address_line_2")
    private String billingAddressLine2;

    @Column(name = "billing_city", nullable = false)
    private String billingCity;

    @Column(name = "billing_state")
    private String billingState;

    @Column(name = "billing_postal_code", nullable = false)
    private String billingPostalCode;

    @Column(name = "billing_country", nullable = false)
    private String billingCountry;

    @Column(name = "billing_phone")
    private String billingPhone;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }

    public boolean isCompleted() {
        return status == OrderStatus.DELIVERED;
    }

    public String getShippingFullName() {
        return shippingFirstName + " " + shippingLastName;
    }

    public String getBillingFullName() {
        return billingFirstName + " " + billingLastName;
    }

    public String getShippingFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(shippingAddressLine1);
        if (shippingAddressLine2 != null && !shippingAddressLine2.trim().isEmpty()) {
            address.append(", ").append(shippingAddressLine2);
        }
        address.append(", ").append(shippingCity);
        if (shippingState != null && !shippingState.trim().isEmpty()) {
            address.append(", ").append(shippingState);
        }
        address.append(" ").append(shippingPostalCode);
        address.append(", ").append(shippingCountry);
        return address.toString();
    }
}