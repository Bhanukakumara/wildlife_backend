package com.example.wildlife_backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {

    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;
}
