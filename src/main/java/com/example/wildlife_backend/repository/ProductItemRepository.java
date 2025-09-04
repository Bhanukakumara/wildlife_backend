package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    
    Optional<ProductItem> findBySku(String sku);
    
    boolean existsBySku(String sku);
    
    List<ProductItem> findByProductId(Long productId);
    
    List<ProductItem> findByProductIdAndQtyInStockGreaterThan(Long productId, Integer qtyInStock);
    
    @Query("SELECT pi FROM ProductItem pi WHERE pi.price BETWEEN :minPrice AND :maxPrice")
    List<ProductItem> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT pi FROM ProductItem pi WHERE pi.qtyInStock > 0")
    List<ProductItem> findAvailableItems();
    
    @Query("SELECT pi FROM ProductItem pi WHERE pi.product.id = :productId AND pi.qtyInStock > 0")
    List<ProductItem> findAvailableItemsByProduct(@Param("productId") Long productId);
    
    @Query("SELECT pi FROM ProductItem pi WHERE pi.sku LIKE %:keyword% OR pi.name LIKE %:keyword%")
    List<ProductItem> searchByKeyword(@Param("keyword") String keyword);
    
    List<ProductItem> findByQtyInStockGreaterThan(Integer qtyInStock);
    
    List<ProductItem> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
