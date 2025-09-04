package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Promotion.PromotionCreateDto;
import com.example.wildlife_backend.dto.Promotion.PromotionGetDto;
import com.example.wildlife_backend.service.PromotionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<PromotionGetDto> createPromotion(@Valid @RequestBody PromotionCreateDto promotionCreateDto) {
        PromotionGetDto createdPromotion = promotionService.createPromotion(promotionCreateDto);
        return new ResponseEntity<>(createdPromotion, HttpStatus.CREATED);
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

    // Get active promotions (based on current date)
    @GetMapping("/active")
    public ResponseEntity<List<PromotionGetDto>> getActivePromotions() {
        List<PromotionGetDto> activePromotions = promotionService.getActivePromotions();
        if (activePromotions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(activePromotions, HttpStatus.OK);
    }

    // Search promotions by name (partial match)
    @GetMapping("/search")
    public ResponseEntity<List<PromotionGetDto>> searchPromotionsByName(@RequestParam String name) {
        List<PromotionGetDto> promotions = promotionService.searchPromotionsByName(name);
        if (promotions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    // Update promotion
    @PutMapping("/{promotionId}")
    public ResponseEntity<PromotionGetDto> updatePromotion(
            @PathVariable Long promotionId,
            @Valid @RequestBody PromotionCreateDto promotionCreateDto) {
        Optional<PromotionGetDto> updatedPromotion = promotionService.updatePromotion(promotionId, promotionCreateDto);
        return updatedPromotion.map(promo -> new ResponseEntity<>(promo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete promotion
    @DeleteMapping("/{promotionId}")
    public ResponseEntity<Boolean> deletePromotion(@PathVariable Long promotionId) {
        boolean deleted = promotionService.deletePromotion(promotionId);
        if (deleted){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Bulk create promotions
    @PostMapping("/bulk-create")
    public ResponseEntity<List<PromotionGetDto>> bulkCreatePromotions(
            @Valid @RequestBody List<PromotionCreateDto> promotionCreateDtos) {
        List<PromotionGetDto> createdPromotions = promotionService.bulkCreatePromotions(promotionCreateDtos);
        return new ResponseEntity<>(createdPromotions, HttpStatus.CREATED);
    }

    // Validate promotion data
    @PostMapping("/validate")
    public ResponseEntity<String> validatePromotion(@Valid @RequestBody PromotionCreateDto promotionCreateDto) {
        boolean isValid = promotionService.validatePromotion(promotionCreateDto);
        if (isValid) {
            return new ResponseEntity<>("Promotion data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid promotion data", HttpStatus.BAD_REQUEST);
    }
}