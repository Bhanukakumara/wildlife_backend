package com.example.wildlife_backend.service;

import com.example.wildlife_backend.entity.User;
import com.example.wildlife_backend.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getEmail(),
                    user.get().getPassword(),
                    Collections.emptyList()
            );
        }
        else  {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
