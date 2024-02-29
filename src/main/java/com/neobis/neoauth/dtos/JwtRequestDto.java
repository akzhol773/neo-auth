package com.neobis.neoauth.dtos;

import lombok.Data;

/**
 * DTO for {@link com.neobis.neoauth.entities.User}
 */

public record JwtRequestDto(String username, String password) {
}