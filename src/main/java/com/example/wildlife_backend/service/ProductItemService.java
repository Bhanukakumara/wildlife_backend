package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ProductItem.ProductItemCreateDto;
import com.example.wildlife_backend.dto.ProductItem.ProductItemGetDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductItemService {
    ProductItemGetDto createProductItem(ProductItemCreateDto productItemCreateDto);
    Optional<ProductItemGetDto> getProductItemById(Long productItemId);
    Optional<ProductItemGetDto> getProductItemBySku(String sku);
    List<ProductItemGetDto> getAllProductItems();
    List<ProductItemGetDto> getProductItemsByProduct(Long productId);
    List<ProductItemGetDto> getAvailableProductItems();
    List<ProductItemGetDto> getAvailableProductItemsByProduct(Long productId);
    List<ProductItemGetDto> searchProductItemsByKeyword(String keyword);
    List<ProductItemGetDto> getProductItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    Optional<ProductItemGetDto> updateProductItem(Long productItemId, ProductItemCreateDto productItemCreateDto);
    boolean deleteProductItem(Long productItemId);
}
