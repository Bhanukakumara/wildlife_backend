package com.example.wildlife_backend.dto.user;

import com.example.wildlife_backend.util.UserRole;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserResponseDto {

    private Long id;
    private String email;
    private String username;
    private String fullName;
    private String firstName;
    private String lastName;
    private String bio;
    private String profileImageUrl;
    private String phoneNumber;
    private String website;
    private String location;
    private Set<UserRole> roles;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private BigDecimal totalEarnings;
    private Integer totalSales;
}
