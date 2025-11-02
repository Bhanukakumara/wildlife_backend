package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.category.CategoryCreateDto;
import com.example.wildlife_backend.dto.category.CategoryResponseDto;
import com.example.wildlife_backend.dto.category.CategoryUpdateDto;
import com.example.wildlife_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Public endpoints
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllActiveCategories() {
        List<CategoryResponseDto> categories = categoryService.getActiveCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/root")
    public ResponseEntity<List<CategoryResponseDto>> getRootCategories() {
        List<CategoryResponseDto> categories = categoryService.getRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<CategoryResponseDto>> getFeaturedCategories() {
        List<CategoryResponseDto> categories = categoryService.getFeaturedCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponseDto>> searchCategories(@RequestParam String keyword) {
        List<CategoryResponseDto> categories = categoryService.searchCategories(keyword);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        Optional<CategoryResponseDto> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponseDto> getCategoryBySlug(@PathVariable String slug) {
        Optional<CategoryResponseDto> category = categoryService.getCategoryBySlug(slug);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryResponseDto>> getSubcategories(@PathVariable Long id) {
        List<CategoryResponseDto> subcategories = categoryService.getSubcategories(id);
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/{id}/photo-count")
    public ResponseEntity<Long> getPhotoCountInCategory(@PathVariable Long id) {
        long count = categoryService.getPhotoCountInCategory(id);
        return ResponseEntity.ok(count);
    }

    // Admin endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryCreateDto categoryDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(createdCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{parentId}/subcategory")
    public ResponseEntity<CategoryResponseDto> createSubcategory(@PathVariable Long parentId, @RequestBody CategoryCreateDto subcategoryDto) {
        CategoryResponseDto createdCategory = categoryService.createSubcategory(parentId, subcategoryDto);
        return ResponseEntity.ok(createdCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateDto categoryDto) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/display-order")
    public ResponseEntity<CategoryResponseDto> updateDisplayOrder(@PathVariable Long id, @RequestParam Integer displayOrder) {
        CategoryResponseDto updatedCategory = categoryService.updateDisplayOrder(id, displayOrder);
        return ResponseEntity.ok(updatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<CategoryResponseDto> activateCategory(@PathVariable Long id) {
        CategoryResponseDto activatedCategory = categoryService.activateCategory(id);
        return ResponseEntity.ok(activatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<CategoryResponseDto> deactivateCategory(@PathVariable Long id) {
        CategoryResponseDto deactivatedCategory = categoryService.deactivateCategory(id);
        return ResponseEntity.ok(deactivatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // Validation endpoints
    @GetMapping("/validate/name")
    public ResponseEntity<Boolean> checkNameAvailability(@RequestParam String name) {
        boolean available = categoryService.isNameAvailable(name);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/validate/slug")
    public ResponseEntity<Boolean> checkSlugAvailability(@RequestParam String slug) {
        boolean available = categoryService.isSlugAvailable(slug);
        return ResponseEntity.ok(available);
    }
}
