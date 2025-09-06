package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ProductItem.ProductItemCreateDto;
import com.example.wildlife_backend.dto.ProductItem.ProductItemGetDto;
import com.example.wildlife_backend.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-items")
@CrossOrigin
@RequiredArgsConstructor
public class ProductItemController {

    private final ProductItemService productItemService;

    // Create a new product item
    @PostMapping("/create")
    public ResponseEntity<ProductItemGetDto> createProductItem(@RequestBody ProductItemCreateDto productItemCreateDto) {
        return new ResponseEntity<>(productItemService.createProductItem(productItemCreateDto), HttpStatus.CREATED);
    }

    // Get product item by ID
    @GetMapping("/{productItemId}")
    public ResponseEntity<ProductItemGetDto> getProductItemById(@PathVariable Long productItemId) {
        Optional<ProductItemGetDto> productItem = productItemService.getProductItemById(productItemId);
        return productItem.map(itemGetDto -> new ResponseEntity<>(itemGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get product item by SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductItemGetDto> getProductItemBySku(@PathVariable String sku) {
        Optional<ProductItemGetDto> productItem = productItemService.getProductItemBySku(sku);
        return productItem.map(itemGetDto -> new ResponseEntity<>(itemGetDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all product items
    @GetMapping("/get-all")
    public ResponseEntity<List<ProductItemGetDto>> getAllProductItems() {
        List<ProductItemGetDto> productItems = productItemService.getAllProductItems();
        if (productItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productItems, HttpStatus.OK);
    }

    // Get product items by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductItemGetDto>> getProductItemsByProduct(@PathVariable Long productId) {
        List<ProductItemGetDto> productItems = productItemService.getProductItemsByProduct(productId);
        if (productItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productItems, HttpStatus.OK);
    }

    // Get available product items
    @GetMapping("/available")
    public ResponseEntity<List<ProductItemGetDto>> getAvailableProductItems() {
        List<ProductItemGetDto> availableItems = productItemService.getAvailableProductItems();
        if (availableItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(availableItems, HttpStatus.OK);
    }

    // Get available product items by product
    @GetMapping("/available/product/{productId}")
    public ResponseEntity<List<ProductItemGetDto>> getAvailableProductItemsByProduct(@PathVariable Long productId) {
        List<ProductItemGetDto> availableItems = productItemService.getAvailableProductItemsByProduct(productId);
        if (availableItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(availableItems, HttpStatus.OK);
    }

    // Search product items by keyword
    @GetMapping("/search")
    public ResponseEntity<List<ProductItemGetDto>> searchProductItems(@RequestParam String keyword) {
        List<ProductItemGetDto> productItems = productItemService.searchProductItemsByKeyword(keyword);
        if (productItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productItems, HttpStatus.OK);
    }

// Update product item
@PutMapping("/{productItemId}")
public ResponseEntity<Void> updateProductItem(@PathVariable Long productItemId, @RequestBody ProductItemCreateDto productItemCreateDto) {
    Optional<ProductItemGetDto> updatedProductItem = productItemService.updateProductItem(productItemId, productItemCreateDto);
    return updatedProductItem.isPresent() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
}

    // Delete product item
    @DeleteMapping("/{productItemId}")
    public ResponseEntity<Void> deleteProductItem(@PathVariable Long productItemId) {
        productItemService.deleteProductItem(productItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
