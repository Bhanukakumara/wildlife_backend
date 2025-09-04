package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ProductCategory.ProductCategoryCreateDto;
import com.example.wildlife_backend.dto.ProductCategory.ProductCategoryGetDto;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {
    ProductCategoryGetDto createProductCategory(ProductCategoryCreateDto productCategoryCreateDto);
    Optional<ProductCategoryGetDto> getProductCategoryById(Long productCategoryId);
    List<ProductCategoryGetDto> getAllProductCategories();
    Optional<ProductCategoryGetDto> updateProductCategory(Long productCategoryId, ProductCategoryCreateDto productCategoryCreateDto);
    boolean deleteProductCategory(Long productCategoryId);
}
