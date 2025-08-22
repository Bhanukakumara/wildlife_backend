package com.example.wildlife_backend.dto.ShopOrder;

import com.example.wildlife_backend.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShopOrderGetDto {
    private Long id;
    private LocalDateTime orderDate;
    private Long userPaymentMethodId;
    private Long shippingAddressId;
    private Long shippingMethodId;
    private BigDecimal orderTotal;
    private OrderStatus orderStatus;
}