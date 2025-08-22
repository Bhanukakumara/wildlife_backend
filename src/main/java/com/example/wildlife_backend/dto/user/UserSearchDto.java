package com.example.wildlife_backend.dto.user;

import com.example.wildlife_backend.util.AccountStatus;
import com.example.wildlife_backend.util.Gender;
import com.example.wildlife_backend.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSearchDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private UserRole role;
    private AccountStatus accountStatus;
}