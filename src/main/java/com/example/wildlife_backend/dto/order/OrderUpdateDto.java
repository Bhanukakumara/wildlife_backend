package com.example.wildlife_backend.dto.order;

import lombok.Data;

@Data
public class OrderUpdateDto {

    private String billingName;
    private String billingEmail;
    private String billingPhone;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private String billingCountry;

    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;

    private String notes;
}
