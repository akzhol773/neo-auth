package com.neobis.neoauth.dtos;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link com.neobis.neoauth.entities.User}
 */
public record UsernameEmailDto(@NotBlank(message = "Email field should not be blank") String email,
                               @NotBlank(message = "Username field should not be blank") String username) implements Serializable {
}