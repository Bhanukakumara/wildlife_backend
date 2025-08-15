package com.example.wildlife_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_payment_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type_id", nullable = false)
    @NotNull(message = "Payment Type is required")
    private PaymentType paymentType;

    private String provider;
    private int accountNumber;
    private LocalDateTime expiryDate;
    private boolean isDefault;

    @OneToMany(mappedBy = "userPaymentMethod", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ShopOrder> shopOrders = new HashSet<>();
}
