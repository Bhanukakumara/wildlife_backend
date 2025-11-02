package com.example.wildlife_backend.repository;

import com.example.wildlife_backend.entity.Payment;
import com.example.wildlife_backend.entity.Order;
import com.example.wildlife_backend.util.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder(Order order);

    List<Payment> findByStatus(PaymentStatus status);

    Optional<Payment> findByOrder_Id(Long orderId);

    Optional<Payment> findByPaymentReference(String paymentReference);
}
