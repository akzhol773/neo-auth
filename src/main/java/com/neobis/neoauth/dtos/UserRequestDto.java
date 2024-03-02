package com.neobis.neoauth.dtos;


import javax.validation.constraints.*;

public record UserRequestDto(
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Username is required")
        @NotNull(message = "Username cannot be null")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should contain only latin letters")
        String username,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,15}$", message = "Password should be valid")
        String password,

        String confirmPassword) {}