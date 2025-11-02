package com.example.wildlife_backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(max = 20, message = "Username must be less than 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 120, message = "Password must be less than 120 characters")
    private String password;

    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(max = 500, message = "Bio must be less than 500 characters")
    private String bio;

    @Size(max = 255, message = "Profile image URL must be less than 255 characters")
    private String profileImageUrl;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Website must be less than 100 characters")
    private String website;

    @Size(max = 100, message = "Location must be less than 100 characters")
    private String location;
}
