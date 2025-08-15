package com.example.wildlife_backend.entity;

import com.example.wildlife_backend.util.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_number")
    @Size(message = "Unit number cannot exceed 20 characters", max = 20)
    private String unitNumber;

    @Column(name = "street_number")
    @Size(max = 50, message = "Street number cannot exceed 50 characters")
    private String streetNumber;

    @Column(name = "address_line_1", nullable = false, length = 100)
    @NotBlank(message = "Address line 1 is required")
    @Size(min = 3, max = 100, message = "Address line 1 must be between 3 and 100 characters")
    private String addressLine1;

    @Column(name = "address_line_2", length = 100)
    @Size(max = 100, message = "Address line 2 cannot exceed 100 characters")
    private String addressLine2;

    @Column(name = "city", nullable = false, length = 50)
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    private String city;

    @Column(name = "state_province", length = 100)
    @Size(max = 100, message = "State/Province cannot exceed 50 characters")
    private String stateProvince;

    @Column(name = "postal_code", nullable = false)
    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(name = "delivery_instructions", length = 500)
    @Size(max = 500, message = "Delivery instructions cannot exceed 500 characters")
    private String deliveryInstructions;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserAddress> userAddresses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @NotNull(message = "Country is required")
    private Country country;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ShopOrder> shopOrders = new HashSet<>();
}
