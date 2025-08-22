package com.example.wildlife_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserUpdatePasswordDto {
    private String oldPassword;
    private String newPassword;
}