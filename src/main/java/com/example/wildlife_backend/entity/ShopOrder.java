package com.example.wildlife_backend.entity;

import com.example.wildlife_backend.util.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    @NotNull(message = "Payment method is required")
    private UserPaymentMethod userPaymentMethod;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    @NotNull(message = "Address is required")
    @JsonBackReference
    private Address address;

    @ManyToOne
    @JoinColumn(name = "shipping_method", nullable = false)
    @NotNull(message = "Shipping Method is required")
    private ShippingMethod shippingMethod;

    @Column(name = "order_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
}

