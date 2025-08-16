package com.example.wildlife_backend.controller;

import com.example.wildlife_backend.dto.Auth.JwtResponse;
import com.example.wildlife_backend.dto.Auth.LoginRequest;
import com.example.wildlife_backend.dto.user.UserGetDto;
import com.example.wildlife_backend.service.JwtService;
import com.example.wildlife_backend.service.UserService;
import com.example.wildlife_backend.util.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        Optional<UserGetDto> user = userService.getUserByEmail(loginRequest.getEmail());
        if (user.isPresent()) {
            String token = jwtService.generateToken(user.get());
            return ResponseEntity.ok(new JwtResponse(token));
        }
        else {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserGetDto> me(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object email = authentication.getPrincipal();
        Optional<UserGetDto> user = userService.getUserByEmail(email.toString());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/role")
    public ResponseEntity<UserRole> getRole(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        UserRole role = jwtService.getRoleFromToken(token);
        return ResponseEntity.ok(role);
    }
}
