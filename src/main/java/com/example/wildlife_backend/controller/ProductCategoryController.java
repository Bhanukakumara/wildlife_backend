package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Product.ProductCategoryCreateDto;
import com.example.wildlife_backend.dto.Product.ProductCategoryGetDto;
import com.example.wildlife_backend.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-categories")
@CrossOrigin
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    // Create a new product category
    @PostMapping
    public ResponseEntity<ProductCategoryGetDto> createCategory(@RequestBody ProductCategoryCreateDto categoryCreateDto) {
        return new ResponseEntity<>(productCategoryService.createCategory(categoryCreateDto), HttpStatus.CREATED);
    }

    // Get category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductCategoryGetDto> getCategoryById(@PathVariable Long categoryId) {
        Optional<ProductCategoryGetDto> category = productCategoryService.getCategoryById(categoryId);
        return category.map(categoryGetDto -> new ResponseEntity<>(categoryGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get category by name
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductCategoryGetDto> getCategoryByName(@PathVariable String name) {
        Optional<ProductCategoryGetDto> category = productCategoryService.getCategoryByName(name);
        return category.map(categoryGetDto -> new ResponseEntity<>(categoryGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<ProductCategoryGetDto>> getAllCategories() {
        List<ProductCategoryGetDto> categories = productCategoryService.getAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Get all active categories
    @GetMapping("/active")
    public ResponseEntity<List<ProductCategoryGetDto>> getAllActiveCategories() {
        List<ProductCategoryGetDto> activeCategories = productCategoryService.getAllActiveCategories();
        if (activeCategories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(activeCategories, HttpStatus.OK);
    }

    // Search categories by keyword
    @GetMapping("/search")
    public ResponseEntity<List<ProductCategoryGetDto>> searchCategories(@RequestParam String keyword) {
        List<ProductCategoryGetDto> categories = productCategoryService.searchCategoriesByKeyword(keyword);
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Update category
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @RequestBody ProductCategoryCreateDto categoryCreateDto) {
        boolean updated = productCategoryService.updateCategory(categoryId, categoryCreateDto);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        productCategoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
