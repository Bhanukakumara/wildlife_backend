package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.OrderItem;
import com.example.wildlife_backend.entity.Order;
import com.example.wildlife_backend.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByPhoto(Photo photo);

    List<OrderItem> findByOrderAndPhoto(Order order, Photo photo);
}
