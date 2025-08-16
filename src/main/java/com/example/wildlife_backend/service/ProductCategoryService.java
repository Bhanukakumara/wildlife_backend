package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Product.ProductCategoryCreateDto;
import com.example.wildlife_backend.dto.Product.ProductCategoryGetDto;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {
    
    // Core CRUD Operations
    ProductCategoryGetDto createCategory(ProductCategoryCreateDto categoryCreateDto);
    Optional<ProductCategoryGetDto> getCategoryById(Long categoryId);
    Optional<ProductCategoryGetDto> getCategoryByName(String name);
    List<ProductCategoryGetDto> getAllCategories();
    List<ProductCategoryGetDto> getAllActiveCategories();
    boolean updateCategory(Long categoryId, ProductCategoryCreateDto categoryDetails);
    boolean deleteCategory(Long categoryId);
    
    // Additional Operations
    List<ProductCategoryGetDto> searchCategoriesByKeyword(String keyword);
    boolean existsByName(String name);
}
