package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Order;
import com.example.wildlife_backend.util.OrderStatus;
import com.example.wildlife_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserAndStatus(User user, OrderStatus status);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> findOrdersByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.status = :status ORDER BY o.createdAt DESC")
    List<Order> findOrdersByStatus(@Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersAfterDate(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED' AND o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED' AND o.createdAt BETWEEN :startDate AND :endDate")
    long countCompletedOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' ORDER BY o.createdAt ASC")
    List<Order> findPendingOrders();

    @Query("SELECT o FROM Order o WHERE o.status = 'PROCESSING' ORDER BY o.createdAt ASC")
    List<Order> findProcessingOrders();

    boolean existsByOrderNumber(String orderNumber);
}
