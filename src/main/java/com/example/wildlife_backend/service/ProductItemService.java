package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.Product.ProductItemCreateDto;
import com.example.wildlife_backend.dto.Product.ProductItemGetDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductItemService {
    
    // Core CRUD Operations
    ProductItemGetDto createProductItem(ProductItemCreateDto productItemCreateDto);
    Optional<ProductItemGetDto> getProductItemById(Long productItemId);
    Optional<ProductItemGetDto> getProductItemBySku(String sku);
    List<ProductItemGetDto> getAllProductItems();
    List<ProductItemGetDto> getProductItemsByProduct(Long productId);
    boolean updateProductItem(Long productItemId, ProductItemCreateDto productItemDetails);
    boolean deleteProductItem(Long productItemId);
    
    // Additional Operations
    List<ProductItemGetDto> getAvailableProductItems();
    List<ProductItemGetDto> getAvailableProductItemsByProduct(Long productId);
    List<ProductItemGetDto> searchProductItemsByKeyword(String keyword);
    List<ProductItemGetDto> getProductItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    boolean existsBySku(String sku);
}
