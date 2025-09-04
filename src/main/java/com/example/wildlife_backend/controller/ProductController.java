package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Product.ProductCreateDto;
import com.example.wildlife_backend.dto.Product.ProductGetDto;
import com.example.wildlife_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
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
        ProductGetDto createdProduct = productService.createProduct(productCreateDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // Get product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetDto> getProductById(@PathVariable Long productId) {
        Optional<ProductGetDto> product = productService.getProductById(productId);
        return product.map(prod -> new ResponseEntity<>(prod, HttpStatus.OK))
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

    // Update product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductGetDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductCreateDto productCreateDto) {
        boolean updateProduct = productService.updateProduct(productId, productCreateDto);
        if (updateProduct) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean deleted = productService.deleteProduct(productId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Bulk create products
    @PostMapping("/bulk-create")
    public ResponseEntity<List<ProductGetDto>> bulkCreateProducts(
            @RequestBody List<ProductCreateDto> productCreateDtos) {
        List<ProductGetDto> createdProducts = productService.bulkCreateProducts(productCreateDtos);
        return new ResponseEntity<>(createdProducts, HttpStatus.CREATED);
    }

    // Validate product data
    @PostMapping("/validate")
    public ResponseEntity<String> validateProduct(@RequestBody ProductCreateDto productCreateDto) {
        boolean isValid = productService.validateProduct(productCreateDto);
        if (isValid) {
            return new ResponseEntity<>("Product data is valid", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid product data", HttpStatus.BAD_REQUEST);
    }
}
