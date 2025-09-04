package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Promotion.PromotionCreateDto;
import com.example.wildlife_backend.dto.Promotion.PromotionGetDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface PromotionService {
    PromotionGetDto createPromotion(PromotionCreateDto promotionCreateDto);
    Optional<PromotionGetDto> getPromotionById(Long promotionId);
    List<PromotionGetDto> getAllPromotions();
    Optional<PromotionGetDto> updatePromotion(Long promotionId, PromotionCreateDto promotionCreateDto);
    boolean deletePromotion(Long promotionId);

    List<PromotionGetDto> getActivePromotions();

    List<PromotionGetDto> searchPromotionsByName(String name);

    List<PromotionGetDto> bulkCreatePromotions(@Valid List<PromotionCreateDto> promotionCreateDtos);

    boolean validatePromotion(@Valid PromotionCreateDto promotionCreateDto);
}