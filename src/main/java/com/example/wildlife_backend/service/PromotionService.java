package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Promotion.PromotionCreateDto;
import com.example.wildlife_backend.dto.Promotion.PromotionGetDto;

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

    List<PromotionGetDto> getPromotionsByCategoryId(Long categoryId);
}