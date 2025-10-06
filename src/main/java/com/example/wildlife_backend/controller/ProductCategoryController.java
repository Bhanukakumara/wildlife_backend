package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ProductCategory.ProductCategoryCreateDto;
import com.example.wildlife_backend.dto.ProductCategory.ProductCategoryGetDto;
import com.example.wildlife_backend.service.ProductCategoryService;
import jakarta.validation.Valid;
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
    @PostMapping("/create")
    public ResponseEntity<ProductCategoryGetDto> createProductCategory(@Valid @RequestBody ProductCategoryCreateDto productCategoryCreateDto) {
        ProductCategoryGetDto createdCategory = productCategoryService.createProductCategory(productCategoryCreateDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Get product category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductCategoryGetDto> getProductCategoryById(@PathVariable Long categoryId) {
        Optional<ProductCategoryGetDto> category = productCategoryService.getProductCategoryById(categoryId);
        return category.map(cat -> new ResponseEntity<>(cat, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all product categories
    @GetMapping
    public ResponseEntity<List<ProductCategoryGetDto>> getAllProductCategories() {
        List<ProductCategoryGetDto> categories = productCategoryService.getAllProductCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Update product category
    @PutMapping("/{categoryId}")
    public ResponseEntity<ProductCategoryGetDto> updateProductCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody ProductCategoryCreateDto productCategoryCreateDto) {
        Optional<ProductCategoryGetDto> updatedCategory = productCategoryService.updateProductCategory(categoryId, productCategoryCreateDto);
        return updatedCategory.map(cat -> new ResponseEntity<>(cat, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete product category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long categoryId) {
        boolean deleted = productCategoryService.deleteProductCategory(categoryId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
