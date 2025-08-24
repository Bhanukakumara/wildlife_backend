package com.example.wildlife_backend.dto.Country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CountryCreateDto {
    private String name;
    private String code;
}
