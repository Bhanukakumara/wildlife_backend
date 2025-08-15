package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ProductCreateDto;
import com.example.wildlife_backend.dto.ProductGetDto;
import com.example.wildlife_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductGetDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        return new ResponseEntity<>(productService.createProduct(productCreateDto), HttpStatus.CREATED);
    }

    // Get product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetDto> getProductById(@PathVariable Long productId) {
        Optional<ProductGetDto> product = productService.getProductById(productId);
        return product.map(productGetDto -> new ResponseEntity<>(productGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get product by name
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductGetDto> getProductByName(@PathVariable String name) {
        Optional<ProductGetDto> product = productService.getProductByName(name);
        return product.map(productGetDto -> new ResponseEntity<>(productGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductGetDto>> getAllProducts() {
        List<ProductGetDto> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Get all active products
    @GetMapping("/active")
    public ResponseEntity<List<ProductGetDto>> getAllActiveProducts() {
        List<ProductGetDto> activeProducts = productService.getAllActiveProducts();
        if (activeProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(activeProducts, HttpStatus.OK);
    }

    // Get all active products with pagination
    @GetMapping("/active/paginated")
    public ResponseEntity<Page<ProductGetDto>> getAllActiveProductsPaginated(Pageable pageable) {
        Page<ProductGetDto> productsPage = productService.getAllActiveProductsPaginated(pageable);
        return new ResponseEntity<>(productsPage, HttpStatus.OK);
    }

    // Get products by category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductGetDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductGetDto> products = productService.getProductsByCategory(categoryId);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Get featured products
    @GetMapping("/featured")
    public ResponseEntity<List<ProductGetDto>> getFeaturedProducts() {
        List<ProductGetDto> featuredProducts = productService.getFeaturedProducts();
        if (featuredProducts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(featuredProducts, HttpStatus.OK);
    }

    // Search products by keyword
    @GetMapping("/search")
    public ResponseEntity<List<ProductGetDto>> searchProducts(@RequestParam String keyword) {
        List<ProductGetDto> products = productService.searchProductsByKeyword(keyword);
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Update product
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @RequestBody ProductCreateDto productCreateDto) {
        boolean updated = productService.updateProduct(productId, productCreateDto);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
