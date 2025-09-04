package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.Promotion.PromotionCreateDto;
import com.example.wildlife_backend.dto.Promotion.PromotionGetDto;
import com.example.wildlife_backend.entity.ProductCategory;
import com.example.wildlife_backend.entity.Promotion;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.wildlife_backend.repository.ProductCategoryRepository;
import com.example.wildlife_backend.repository.PromotionRepository;
import com.example.wildlife_backend.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public PromotionGetDto createPromotion(PromotionCreateDto promotionCreateDto) {
        Promotion promotion = new Promotion();
        promotion.setName(promotionCreateDto.getName());
        promotion.setDescription(promotionCreateDto.getDescription());
        promotion.setDiscount_rate(promotionCreateDto.getDiscountRate());
        promotion.setStartDate(promotionCreateDto.getStartDate());
        promotion.setEndDate(promotionCreateDto.getEndDate());
        
        // Set product categories if provided
        if (promotionCreateDto.getProductCategoryIds() != null && !promotionCreateDto.getProductCategoryIds().isEmpty()) {
            Set<ProductCategory> categories = new HashSet<>();
            for (Long categoryId : promotionCreateDto.getProductCategoryIds()) {
                ProductCategory category = productCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("ProductCategory not found with id: " + categoryId));
                categories.add(category);
            }
            promotion.setProduct_category(categories);
        }
        
        Promotion savedPromotion = promotionRepository.save(promotion);
        return convertToGetDto(savedPromotion);
    }

    @Override
    public Optional<PromotionGetDto> getPromotionById(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .map(this::convertToGetDto);
    }

    @Override
    public List<PromotionGetDto> getAllPromotions() {
        return promotionRepository.findAll()
                .stream()
                .map(this::convertToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PromotionGetDto> updatePromotion(Long promotionId, PromotionCreateDto promotionCreateDto) {
        Promotion existingPromotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + promotionId));
        
        existingPromotion.setName(promotionCreateDto.getName());
        existingPromotion.setDescription(promotionCreateDto.getDescription());
        existingPromotion.setDiscount_rate(promotionCreateDto.getDiscountRate());
        existingPromotion.setStartDate(promotionCreateDto.getStartDate());
        existingPromotion.setEndDate(promotionCreateDto.getEndDate());
        
        // Update product categories if provided
        if (promotionCreateDto.getProductCategoryIds() != null) {
            Set<ProductCategory> categories = new HashSet<>();
            for (Long categoryId : promotionCreateDto.getProductCategoryIds()) {
                ProductCategory category = productCategoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("ProductCategory not found with id: " + categoryId));
                categories.add(category);
            }
            existingPromotion.setProduct_category(categories);
        }
        
        Promotion updatedPromotion = promotionRepository.save(existingPromotion);
        return Optional.of(convertToGetDto(updatedPromotion));
    }

@Override
public boolean deletePromotion(Long promotionId) {
    if (promotionRepository.existsById(promotionId)) {
        promotionRepository.deleteById(promotionId);
        return true;
    }
    return false;
}

@Override
public List<PromotionGetDto> getActivePromotions() {
    LocalDateTime now = LocalDateTime.now();
    return promotionRepository.findByStartDateBeforeAndEndDateAfter(now, now)
            .stream()
            .map(this::convertToGetDto)
            .collect(Collectors.toList());
}

@Override
public List<PromotionGetDto> searchPromotionsByName(String name) {
    return promotionRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(this::convertToGetDto)
            .collect(Collectors.toList());
}

@Override
public List<PromotionGetDto> bulkCreatePromotions(List<PromotionCreateDto> promotionCreateDtos) {
    List<Promotion> promotions = promotionCreateDtos.stream()
            .map(dto -> {
                Promotion promotion = new Promotion();
                promotion.setName(dto.getName());
                promotion.setDescription(dto.getDescription());
                promotion.setDiscount_rate(dto.getDiscountRate());
                promotion.setStartDate(dto.getStartDate());
                promotion.setEndDate(dto.getEndDate());

                // Set product categories if provided
                if (dto.getProductCategoryIds() != null && !dto.getProductCategoryIds().isEmpty()) {
                    Set<ProductCategory> categories = new HashSet<>();
                    for (Long categoryId : dto.getProductCategoryIds()) {
                        ProductCategory category = productCategoryRepository.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("ProductCategory not found with id: " + categoryId));
                        categories.add(category);
                    }
                    promotion.setProduct_category(categories);
                }

                return promotion;
            })
            .collect(Collectors.toList());

    List<Promotion> savedPromotions = promotionRepository.saveAll(promotions);
    return savedPromotions.stream()
            .map(this::convertToGetDto)
            .collect(Collectors.toList());
}

@Override
public boolean validatePromotion(PromotionCreateDto promotionCreateDto) {
    // Basic validation logic
    if (promotionCreateDto.getName() == null || promotionCreateDto.getName().isEmpty()) {
        return false;
    }
    if (promotionCreateDto.getDiscountRate() == null || promotionCreateDto.getDiscountRate().compareTo(BigDecimal.ZERO) <= 0) {
        return false;
    }
    if (promotionCreateDto.getStartDate() == null || promotionCreateDto.getEndDate() == null) {
        return false;
    }
    if (promotionCreateDto.getStartDate().isAfter(promotionCreateDto.getEndDate())) {
        return false;
    }
    return true;
}

    private PromotionGetDto convertToGetDto(Promotion promotion) {
        Set<Long> categoryIds = promotion.getProduct_category() != null ? 
            promotion.getProduct_category().stream()
                .map(ProductCategory::getId)
                .collect(Collectors.toSet()) : 
            new HashSet<>();
            
        return PromotionGetDto.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .description(promotion.getDescription())
                .discountRate(promotion.getDiscount_rate())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .productCategoryIds(categoryIds)
                .build();
    }
}
