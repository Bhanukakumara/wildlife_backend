package com.example.wildlife_backend.dto.ProductItem;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductItemCreateDto {
    @NotBlank(message = "Product Item name is required")
    @Size(max = 100, message = "Product Item name must be less than 100 characters")
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 64, message = "SKU must be at most 64 characters")
    private String sku;

    @NotBlank(message = "Description is required")
    private String description;

    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 2 decimal places")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @Digits(integer = 8, fraction = 2, message = "Weight must have up to 2 decimal places")
    private BigDecimal weight;

    @Size(max = 10, message = "Weight unit must be less than 10 characters")
    private String weightUnit;

    @Digits(integer = 8, fraction = 2, message = "Length must have up to 2 decimal places")
    private BigDecimal length;

    @Digits(integer = 8, fraction = 2, message = "Width must have up to 2 decimal places")
    private BigDecimal width;

    @Digits(integer = 8, fraction = 2, message = "Height must have up to 2 decimal places")
    private BigDecimal height;

    @NotNull(message = "Customizable is required")
    private boolean customizable;

    @NotNull(message = "Free shipping is required")
    private boolean freeShipping;

    @Min(value = 0, message = "Quantity cannot be negative")
    @NotNull(message = "Quantity in stock is required")
    private Integer qtyInStock;

    @Size(max = 255, message = "Image URL must be at most 255 characters")
    private String imageUrl;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
