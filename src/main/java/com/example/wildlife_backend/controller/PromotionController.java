package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Promotion.PromotionCreateDto;
import com.example.wildlife_backend.dto.Promotion.PromotionGetDto;
import com.example.wildlife_backend.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    // Create a new promotion
    @PostMapping
    public ResponseEntity<PromotionGetDto> createPromotion(@RequestBody PromotionCreateDto promotionCreateDto) {
        return new ResponseEntity<>(promotionService.createPromotion(promotionCreateDto), HttpStatus.CREATED);
    }

    // Get promotion by ID
    @GetMapping("/{promotionId}")
    public ResponseEntity<PromotionGetDto> getPromotionById(@PathVariable Long promotionId) {
        Optional<PromotionGetDto> promotion = promotionService.getPromotionById(promotionId);
        return promotion.map(promo -> new ResponseEntity<>(promo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all promotions
    @GetMapping
    public ResponseEntity<List<PromotionGetDto>> getAllPromotions() {
        List<PromotionGetDto> promotions = promotionService.getAllPromotions();
        if (promotions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    // Update promotion
    @PutMapping("/{promotionId}")
    public ResponseEntity<PromotionGetDto> updatePromotion(@PathVariable Long promotionId, @RequestBody PromotionCreateDto promotionCreateDto) {
        Optional<PromotionGetDto> updatedPromotion = promotionService.updatePromotion(promotionId, promotionCreateDto);
        return updatedPromotion.map(promo -> new ResponseEntity<>(promo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete promotion
    @DeleteMapping("/{promotionId}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long promotionId) {
        try {
            promotionService.deletePromotion(promotionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}