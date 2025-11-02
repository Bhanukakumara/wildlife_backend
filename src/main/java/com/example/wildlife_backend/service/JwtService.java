package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.user.UserResponseDto;
import com.example.wildlife_backend.util.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.tokenExpiration}")
    private Long tokenExpiration;
    @Value("${spring.jwt.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    public String generateToken(UserResponseDto userGetDto) {
        String role = userGetDto.getRoles().iterator().next().name(); // Take first role
        return Jwts.builder()
                .subject(userGetDto.getEmail())
                .claim("roles", role)
                .claim("name", userGetDto.getFullName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        }
        catch (JwtException _){
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public UserRole getRoleFromToken(String token) {
        return UserRole.valueOf(getClaims(token).get("roles", String.class));
    }

    public String generateRefreshToken(UserResponseDto userGetDto) {
        return Jwts.builder()
                .subject(userGetDto.getEmail())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * refreshTokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = getClaims(refreshToken);
            return claims.getExpiration().after(new Date()) && "refresh".equals(claims.get("type"));
        }
        catch (JwtException _){
            return false;
        }
    }

    public String getEmailFromRefreshToken(String refreshToken) {
        Claims claims = getClaims(refreshToken);
        if ("refresh".equals(claims.get("type"))) {
            return claims.getSubject();
        }
        throw new JwtException("Invalid refresh token");
    }

    public Long getTokenExpiration() {
        return tokenExpiration;
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}