package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.auth.LoginResponseDto;
import com.example.wildlife_backend.dto.auth.RefreshTokenRequestDto;
import com.example.wildlife_backend.dto.user.UserLoginDto;
import com.example.wildlife_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDto loginDto) {
        LoginResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto refreshDto) {
        LoginResponseDto response = authService.refreshToken(refreshDto.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
