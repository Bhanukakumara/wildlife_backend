package com.example.wildlife_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @OneToMany(mappedBy = "paymentType", cascade = CascadeType.ALL)
    private Set<UserPaymentMethod> userPaymentMethods = new HashSet<UserPaymentMethod>();
}
