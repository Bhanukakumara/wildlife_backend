package com.example.wildlife_backend.service;

import com.example.wildlife_backend.dto.user.UserGetDto;
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
    public String generateToken(UserGetDto userGetDto) {
        return Jwts.builder()
                .subject(userGetDto.getEmail())
                .claim("role", userGetDto.getRole())
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
        return UserRole.valueOf(getClaims(token).get("role", String.class));
    }
}