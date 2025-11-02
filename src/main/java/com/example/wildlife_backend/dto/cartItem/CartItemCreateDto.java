package com.example.wildlife_backend.dto.cartItem;

import com.example.wildlife_backend.util.LicenseType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemCreateDto {

    @NotNull(message = "Photo ID is required")
    private Long photoId;

    @NotNull(message = "License type is required")
    private LicenseType licenseType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
}
