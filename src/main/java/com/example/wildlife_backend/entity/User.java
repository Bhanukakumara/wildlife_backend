package com.example.wildlife_backend.entity;

import com.example.wildlife_backend.util.AccountStatus;
import com.example.wildlife_backend.util.Gender;
import com.example.wildlife_backend.util.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Column(name = "middle_name", length = 50)
    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Column(name = "display_name", length = 100, nullable = false)
    @NotBlank(message = "Display name is required")
    @Size(max = 100, message = "Display name cannot exceed 100 characters")
    private String displayName;

    @Column(name = "profile_picture", columnDefinition = "TEXT")
    private String profilePicture;

    @Column(name = "password", nullable = false, length = 255)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Column(name = "phone_number", unique = true, length = 20)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;

    @Column(name = "deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> userAddresses;

    @OneToMany(mappedBy = "user")
    private Set<UserPaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserReview> reviews = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private ShoppingCart shoppingCart;
}
