package com.neobis.neoauth.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public record UserRequestDto(
        @NotNull (message = "Email field should not be null")
        @NotBlank(message = "Email field should not be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Username is required")
        @NotNull(message = "Username cannot be null")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should contain only latin letters")
        String username,

        @NotNull (message = "Password field should not be null")
        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,15}$", message = "Password should be valid")
        String password,

        String confirmPassword) {}