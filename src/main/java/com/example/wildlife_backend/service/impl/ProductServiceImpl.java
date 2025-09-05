package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.Product.ProductCreateDto;
import com.example.wildlife_backend.dto.Product.ProductGetDto;
import com.example.wildlife_backend.dto.Product.ProductItemGetDto;
import com.example.wildlife_backend.dto.ProductCategory.ProductCategoryGetDto;
import com.example.wildlife_backend.entity.Product;
import com.example.wildlife_backend.entity.ProductCategory;
import com.example.wildlife_backend.exception.DuplicateResourceException;
import com.example.wildlife_backend.exception.ResourceNotFoundException;
import com.example.wildlife_backend.repository.ProductCategoryRepository;
import com.example.wildlife_backend.repository.ProductRepository;
import com.example.wildlife_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    @Transactional
    public ProductGetDto createProduct(ProductCreateDto productCreateDto) {
        validateProductCreation(productCreateDto);

        Product product = convertCreateDtoToProduct(productCreateDto);
        Product savedProduct = productRepository.save(product);

        log.info("Created new product with ID: {}", savedProduct.getId());
        return convertProductToGetDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductGetDto> getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(this::convertProductToGetDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductGetDto> getProductByName(String name) {
        return productRepository.findByName(name)
                .map(this::convertProductToGetDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductGetDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return products.stream()
                .map(this::convertProductToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateProduct(Long productId, ProductCreateDto productDetails) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        validateProductUpdate(existingProduct, productDetails);
        updateProductFromDto(existingProduct, productDetails);
        productRepository.save(existingProduct);

        log.info("Updated product with ID: {}", productId);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        productRepository.delete(product);
        log.info("Deleted product with ID: {}", productId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    private void validateProductCreation(ProductCreateDto productCreateDto) {
        if (productRepository.existsByName(productCreateDto.getName())) {
            throw new DuplicateResourceException("Product name already exists: " + productCreateDto.getName());
        }

        if (productCreateDto.getCategoryId() != null) {
            productCategoryRepository.findById(productCreateDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productCreateDto.getCategoryId()));
        }
    }

    private void validateProductUpdate(Product existingProduct, ProductCreateDto productDetails) {
        if (!existingProduct.getName().equals(productDetails.getName()) &&
                productRepository.existsByName(productDetails.getName())) {
            throw new DuplicateResourceException("Product name already exists: " + productDetails.getName());
        }

        if (productDetails.getCategoryId() != null) {
            productCategoryRepository.findById(productDetails.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDetails.getCategoryId()));
        }
    }

    private Product convertCreateDtoToProduct(ProductCreateDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrimaryImageUrl(dto.getPrimaryImageUrl());
        product.setActive(dto.isActive());
        product.setFeatured(dto.isFeatured());
        product.setMetaTitle(dto.getMetaTitle());
        product.setMetaDescription(dto.getMetaDescription());
        product.setMetaKeywords(dto.getMetaKeywords());

        if (dto.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
            product.setCategory(category);
        }

        return product;
    }

    private ProductGetDto convertProductToGetDto(Product product) {
        ProductCategoryGetDto categoryDto = null;
        if (product.getCategory() != null) {
            categoryDto = ProductCategoryGetDto.builder()
                    .id(product.getCategory().getId())
                    .name(product.getCategory().getName())
                    .description(product.getCategory().getDescription())
                    .imageUrl(product.getCategory().getImageUrl())
                    .active(product.getCategory().isActive())
                    .createdAt(product.getCategory().getCreatedAt())
                    .updatedAt(product.getCategory().getUpdatedAt())
                    .createdBy(product.getCategory().getCreatedBy())
                    .updatedBy(product.getCategory().getUpdatedBy())
                    .build();
        }

        Set<ProductItemGetDto> productItemDtos = product.getProductItems().stream()
                .map(item -> ProductItemGetDto.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .sku(item.getSku())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .weight(item.getWeight())
                        .weightUnit(item.getWeightUnit())
                        .length(item.getLength())
                        .width(item.getWidth())
                        .height(item.getHeight())
                        .customizable(item.isCustomizable())
                        .freeShipping(item.isFreeShipping())
                        .qtyInStock(item.getQtyInStock())
                        .imageUrl(item.getImageUrl())
                        .createdAt(item.getCreatedAt())
                        .updatedAt(item.getUpdatedAt())
                        .productId(product.getId())
                        .build())
                .collect(Collectors.toSet());

        return ProductGetDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .primaryImageUrl(product.getPrimaryImageUrl())
                .active(product.isActive())
                .featured(product.isFeatured())
                .metaTitle(product.getMetaTitle())
                .metaDescription(product.getMetaDescription())
                .metaKeywords(product.getMetaKeywords())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .category(categoryDto)
                .productItems(productItemDtos)
                .build();
    }

    private void updateProductFromDto(Product product, ProductCreateDto dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrimaryImageUrl(dto.getPrimaryImageUrl());
        product.setActive(dto.isActive());
        product.setFeatured(dto.isFeatured());
        product.setMetaTitle(dto.getMetaTitle());
        product.setMetaDescription(dto.getMetaDescription());
        product.setMetaKeywords(dto.getMetaKeywords());

        if (dto.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
            product.setCategory(category);
        }
    }

    @Override
    @Transactional
    public List<ProductGetDto> bulkCreateProducts(List<ProductCreateDto> productCreateDtos) {
        List<Product> products = productCreateDtos.stream()
                .map(this::convertCreateDtoToProduct)
                .collect(Collectors.toList());

        List<Product> savedProducts = productRepository.saveAll(products);
        return savedProducts.stream()
                .map(this::convertProductToGetDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateProduct(ProductCreateDto productCreateDto) {
        // Basic validation logic
//        if (productCreateDto.getName() == null || productCreateDto.getName().isEmpty()) {
//            return false;
//        }
//        if (productCreateDto.getPrice() == null || productCreateDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
//            return false;
//        }
//        if (productCreateDto.getQtyInStock() == null || productCreateDto.getQtyInStock() < 0) {
//            return false;
//        }
        return true;
    }
}
