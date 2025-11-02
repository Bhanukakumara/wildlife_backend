package com.example.wildlife_backend.dto.cartItem;

import com.example.wildlife_backend.util.LicenseType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItemDto {

    private Long id;
    private Long photoId;
    private String photoTitle;
    private String photoImageUrl;
    private BigDecimal photoBasePrice;
    private LicenseType licenseType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private LocalDateTime addedAt;
}
