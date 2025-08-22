package com.example.wildlife_backend.dto.Promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PromotionCreateDto {
    private String name;
    private String description;
    private BigDecimal discountRate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Set<Long> productCategoryIds;
}