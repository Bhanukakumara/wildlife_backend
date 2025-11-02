package com.example.wildlife_backend.entity;


import com.example.wildlife_backend.util.OrderStatus;
import com.example.wildlife_backend.util.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "orders",
    indexes = {
        @Index(name = "idx_order_user_id", columnList = "user_id"),
        @Index(name = "idx_order_status", columnList = "status"),
        @Index(name = "idx_order_order_number", columnList = "order_number"),
        @Index(name = "idx_order_created_at", columnList = "created_at")
    }
)
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "subtotal_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalAmount;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "shipping_amount", precision = 10, scale = 2)
    private BigDecimal shippingAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "currency", nullable = false)
    private String currency = "USD";

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "billing_name")
    private String billingName;

    @Column(name = "billing_email")
    private String billingEmail;

    @Column(name = "billing_phone")
    private String billingPhone;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "billing_city")
    private String billingCity;

    @Column(name = "billing_state")
    private String billingState;

    @Column(name = "billing_zip")
    private String billingZip;

    @Column(name = "billing_country")
    private String billingCountry;

    @Column(name = "shipping_name")
    private String shippingName;

    @Column(name = "shipping_phone")
    private String shippingPhone;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_city")
    private String shippingCity;

    @Column(name = "shipping_state")
    private String shippingState;

    @Column(name = "shipping_zip")
    private String shippingZip;

    @Column(name = "shipping_country")
    private String shippingCountry;

    @Column(name = "notes")
    private String notes;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Generate order number if not set
        if (orderNumber == null) {
            orderNumber = "ORD-" + System.currentTimeMillis();
        }
        // Calculate total if not set
        if (totalAmount == null && subtotalAmount != null) {
            totalAmount = subtotalAmount.add(taxAmount).add(shippingAmount).subtract(discountAmount);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotal();
    }

    public void removeOrderItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        recalculateTotal();
    }

    private void recalculateTotal() {
        if (subtotalAmount == null) {
            subtotalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (totalAmount == null) {
            totalAmount = subtotalAmount.add(taxAmount).add(shippingAmount).subtract(discountAmount);
        }
    }
}
