package com.example.wildlife_backend.dto.wishList;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WishlistResponseDto {

    private Long id;
    private Long userId;
    private String userUsername;
    private String userFullName;
    private Long photoId;
    private String photoTitle;
    private String photoImageUrl;
    private BigDecimal photoBasePrice;
    private LocalDateTime addedAt;
}
