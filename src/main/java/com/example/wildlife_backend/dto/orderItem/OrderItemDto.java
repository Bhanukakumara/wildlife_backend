package com.example.wildlife_backend.dto.orderItem;

import com.example.wildlife_backend.util.LicenseType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItemDto {

    private Long id;
    private Long photoId;
    private String photoTitle;
    private String photoImageUrl;
    private LicenseType licenseType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private BigDecimal licensePrice;
    private String downloadUrl;
    private Integer downloadsRemaining;
    private Integer downloadExpiryDays;
    private LocalDateTime expiresAt;
    private Boolean isDownloaded;
    private LocalDateTime downloadedAt;
    private Integer downloadCount;
}
