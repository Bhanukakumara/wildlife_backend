package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    List<OrderLine> findByShopOrderId(Long shopOrderId);
    List<OrderLine> findByProductItemId(Long productItemId);
}
