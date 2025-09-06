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
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "SKU is required")
    private String sku;

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

    private boolean freeShipping;

    @Min(value = 0, message = "Quantity cannot be negative")
    @NotNull(message = "Quantity in stock is required")
    private Integer qtyInStock;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Category ID is required")
    private String categoryId;
}