package com.example.wildlife_backend.entity;

import com.example.wildlife_backend.util.LicenseType;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order_items",
    indexes = {
        @Index(name = "idx_order_item_order_id", columnList = "order_id"),
        @Index(name = "idx_order_item_photo_id", columnList = "photo_id")
    }
)
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type", nullable = false)
    private LicenseType licenseType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "license_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal licensePrice;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "downloads_remaining")
    private Integer downloadsRemaining;

    @Column(name = "download_expiry_days")
    private Integer downloadExpiryDays;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_downloaded")
    private Boolean isDownloaded = false;

    @Column(name = "downloaded_at")
    private LocalDateTime downloadedAt;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @PrePersist
    protected void onPrePersist() {
        if (totalPrice == null && unitPrice != null && quantity != null) {
            totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        if (expiresAt == null && downloadExpiryDays != null) {
            expiresAt = LocalDateTime.now().plusDays(downloadExpiryDays);
        }
        if (downloadsRemaining == null) {
            downloadsRemaining = quantity * 3; // Default 3 downloads per item
        }
    }

    @PreUpdate
    protected void onPreUpdate() {
        if (unitPrice != null && quantity != null) {
            totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
