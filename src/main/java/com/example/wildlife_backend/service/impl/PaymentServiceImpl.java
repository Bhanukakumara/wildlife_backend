package com.example.wildlife_backend.service.impl;

import com.example.wildlife_backend.dto.payment.PaymentCreateDto;
import com.example.wildlife_backend.dto.payment.PaymentResponseDto;
import com.example.wildlife_backend.entity.Order;
import com.example.wildlife_backend.entity.Payment;
import com.example.wildlife_backend.util.PaymentStatus;
import com.example.wildlife_backend.repository.OrderRepository;
import com.example.wildlife_backend.repository.PaymentRepository;
import com.example.wildlife_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    public PaymentResponseDto createPayment(PaymentCreateDto paymentDto) {
        Order order = orderRepository.findById(paymentDto.getOrderId())
            .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setAmount(paymentDto.getAmount());
        payment.setCurrency(paymentDto.getCurrency());

        Payment savedPayment = createPaymentEntity(payment);
        return convertToPaymentResponseDto(savedPayment);
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(status);

        if (status == PaymentStatus.COMPLETED || status == PaymentStatus.FAILED) {
            payment.setProcessedAt(LocalDateTime.now());
        }

        Payment savedPayment = paymentRepository.save(payment);
        return convertToPaymentResponseDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentResponseDto> getPaymentById(Long id) {
        return paymentRepository.findById(id)
            .map(this::convertToPaymentResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentResponseDto> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrder_Id(orderId)
            .map(this::convertToPaymentResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentResponseDto> getPaymentByReference(String paymentReference) {
        return paymentRepository.findByPaymentReference(paymentReference)
            .map(this::convertToPaymentResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
            .map(this::convertToPaymentResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream()
            .map(this::convertToPaymentResponseDto)
            .collect(Collectors.toList());
    }

    private Payment createPaymentEntity(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    private PaymentResponseDto convertToPaymentResponseDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setPaymentReference(payment.getPaymentReference());
        dto.setOrderId(payment.getOrder().getId());
        dto.setOrderNumber(payment.getOrder().getOrderNumber());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setStatus(payment.getStatus());
        dto.setGatewayTransactionId(payment.getGatewayTransactionId());
        dto.setGatewayResponse(payment.getGatewayResponse());
        dto.setFailureReason(payment.getFailureReason());
        dto.setProcessedAt(payment.getProcessedAt());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}
