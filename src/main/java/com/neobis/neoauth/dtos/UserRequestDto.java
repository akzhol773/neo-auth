package com.neobis.neoauth.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.neobis.neoauth.entities.User}
 */


public record UserRequestDto(String email, String username, String password, String confirmPassword) {
}