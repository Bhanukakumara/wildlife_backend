package com.example.wildlife_backend.dto;

import com.example.wildlife_backend.entity.Country;
import com.example.wildlife_backend.util.AddressType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateDto {
    private String unitNumber;
    private String streetNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private AddressType addressType;
    private String deliveryInstructions;
    private Country country;
    private boolean isDefault;
}
