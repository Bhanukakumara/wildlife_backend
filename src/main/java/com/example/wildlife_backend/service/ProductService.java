package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.ProductCreateDto;
import com.example.wildlife_backend.dto.ProductGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    // Core CRUD Operations
    ProductGetDto createProduct(ProductCreateDto productCreateDto);
    Optional<ProductGetDto> getProductById(Long productId);
    Optional<ProductGetDto> getProductByName(String name);
    List<ProductGetDto> getAllProducts();
    List<ProductGetDto> getAllActiveProducts();
    boolean updateProduct(Long productId, ProductCreateDto productDetails);
    boolean deleteProduct(Long productId);
    
    // Additional Operations
    List<ProductGetDto> getProductsByCategory(Long categoryId);
    List<ProductGetDto> getFeaturedProducts();
    List<ProductGetDto> searchProductsByKeyword(String keyword);
    Page<ProductGetDto> getAllActiveProductsPaginated(Pageable pageable);
    boolean existsByName(String name);
}
