package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.ProductItem.ProductItemCreateDto;
import com.example.wildlife_backend.dto.ProductItem.ProductItemGetDto;
import com.example.wildlife_backend.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-items")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ProductItemController {

    private final ProductItemService productItemService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductItemGetDto> createProductItem(
            @RequestPart("productItem") @Valid ProductItemCreateDto productItemCreateDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            ProductItemGetDto createdItem = productItemService.createProductItem(productItemCreateDto, imageFile);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productItemId}")
    public ResponseEntity<ProductItemGetDto> getProductItemById(@PathVariable Long productItemId) {
        Optional<ProductItemGetDto> productItem = productItemService.getProductItemById(productItemId);
        return productItem.map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductItemGetDto> getProductItemBySku(@PathVariable String sku) {
        Optional<ProductItemGetDto> productItem = productItemService.getProductItemBySku(sku);
        return productItem.map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductItemGetDto>> getAllProductItems() {
        List<ProductItemGetDto> productItems = productItemService.getAllProductItems();
        return productItems.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(productItems, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductItemGetDto>> getProductItemsByProduct(@PathVariable Long productId) {
        List<ProductItemGetDto> productItems = productItemService.getProductItemsByProduct(productId);
        return productItems.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(productItems, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ProductItemGetDto>> getAvailableProductItems() {
        List<ProductItemGetDto> availableItems = productItemService.getAvailableProductItems();
        return availableItems.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(availableItems, HttpStatus.OK);
    }

    @GetMapping("/available/product/{productId}")
    public ResponseEntity<List<ProductItemGetDto>> getAvailableProductItemsByProduct(@PathVariable Long productId) {
        List<ProductItemGetDto> availableItems = productItemService.getAvailableProductItemsByProduct(productId);
        return availableItems.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(availableItems, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductItemGetDto>> searchProductItems(@RequestParam String keyword) {
        List<ProductItemGetDto> productItems = productItemService.searchProductItemsByKeyword(keyword);
        return productItems.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(productItems, HttpStatus.OK);
    }

    @PutMapping("/{productItemId}")
    public ResponseEntity<ProductItemGetDto> updateProductItem(
            @PathVariable Long productItemId,
            @RequestBody @Valid ProductItemCreateDto productItemCreateDto) {
        Optional<ProductItemGetDto> updatedProductItem = productItemService.updateProductItem(productItemId, productItemCreateDto);
        return updatedProductItem.map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{productItemId}")
    public ResponseEntity<Void> deleteProductItem(@PathVariable Long productItemId) {
        try {
            productItemService.deleteProductItem(productItemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}