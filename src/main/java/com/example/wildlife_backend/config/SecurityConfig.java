package com.example.wildlife_backend.config;

import com.example.wildlife_backend.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(c->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->auth
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/user/create-list").permitAll()
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/api/auth/user/create").hasRole(UserRole.ADMIN.name())
//                        .requestMatchers(HttpMethod.GET,"/api/auth/user/get-all").hasRole(UserRole.ADMIN.name())
//                        .requestMatchers(HttpMethod.GET, "/api/auth/user/get-by-id/**").hasRole(UserRole.ADMIN.name())
//                        .requestMatchers(HttpMethod.GET,"/api/user/get-by-email/**").hasRole(UserRole.ADMIN.name())
//                        .requestMatchers(HttpMethod.GET,"/api/user/get-by-role/**").hasRole(UserRole.ADMIN.name())
//                        .requestMatchers(HttpMethod.POST, "/api/auth/course/create").hasRole(UserRole.ADMIN.name())
//                        .requestMatchers(HttpMethod.GET, "/api/auth/course/get-all").hasAnyRole(UserRole.ADMIN.name(), UserRole.TEACHER.name())
//                        .requestMatchers(HttpMethod.GET,"/api/course/get-by-id/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.TEACHER.name())
//                        .requestMatchers(HttpMethod.GET,"/api/course/get-by-userId/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.TEACHER.name())
//                        .requestMatchers(HttpMethod.POST, "/api/auth/exam/create").hasAnyRole(UserRole.ADMIN.name(), UserRole.TEACHER.name())
//                        .requestMatchers(HttpMethod.GET, "/api/auth/exam/get-by-teacherId/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.TEACHER.name())
//                        .requestMatchers(HttpMethod.POST, "/api/auth/question/create").hasAnyRole(UserRole.ADMIN.name(), UserRole.TEACHER.name())
//                        .requestMatchers(HttpMethod.POST, "/api/auth/enrollment/create").hasRole(UserRole.STUDENT.name())
//                        .requestMatchers(HttpMethod.POST, "/api/auth/paper/save").hasRole(UserRole.STUDENT.name())
//                        .requestMatchers(HttpMethod.PUT, "/api/auth/exam/update/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.TEACHER.name())
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
