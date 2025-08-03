package com.example.wildlife_backend.dto;

import com.example.wildlife_backend.util.UserStatus;
import com.example.wildlife_backend.util.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserCreateDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    private UserType userType;
    private UserStatus userStatus;
}
