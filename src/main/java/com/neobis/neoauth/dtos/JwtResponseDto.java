package com.neobis.neoauth.dtos;


import lombok.Data;

/**
 * DTO for {@link com.neobis.neoauth.entities.User}
 */

public record JwtResponseDto(String username, String accessToken, String refreshToken){
}