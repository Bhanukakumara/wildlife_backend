package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.auth.LoginResponseDto;
import com.example.wildlife_backend.dto.user.UserLoginDto;
import com.example.wildlife_backend.dto.user.UserResponseDto;
import com.example.wildlife_backend.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public LoginResponseDto login(UserLoginDto loginDto) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
        );

        // Get user details - authentication.getName() returns the username/email used for auth
        String identifier = authentication.getName();
        Optional<User> userEntity = userService.getUserByEmail(identifier);
        if (userEntity.isEmpty()) {
            userEntity = userService.getUserByUsername(identifier);
        }
        if (userEntity.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<UserResponseDto> userOpt = userService.getUserResponseById(userEntity.get().getId());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User details not found");
        }

        UserResponseDto user = userOpt.get();

        // Update last login
        userService.updateLastLogin(user.getId());

        // Generate tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Calculate expiration times
        LocalDateTime accessExpiresAt = LocalDateTime.now().plusSeconds(jwtService.getTokenExpiration());
        LocalDateTime refreshExpiresAt = LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpiration());

        return new LoginResponseDto(accessToken, refreshToken, user, accessExpiresAt, refreshExpiresAt);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtService.getEmailFromRefreshToken(refreshToken);
        Optional<UserResponseDto> userOpt = userService.getUserResponseById(
                userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found")).getId()
        );

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User details not found");
        }

        UserResponseDto user = userOpt.get();

        // Generate new tokens
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        LocalDateTime accessExpiresAt = LocalDateTime.now().plusSeconds(jwtService.getTokenExpiration());
        LocalDateTime refreshExpiresAt = LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpiration());

        return new LoginResponseDto(newAccessToken, newRefreshToken, user, accessExpiresAt, refreshExpiresAt);
    }
}
