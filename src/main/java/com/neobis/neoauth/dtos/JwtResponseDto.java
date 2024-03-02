package com.neobis.neoauth.dtos;


import lombok.Builder;
import lombok.Data;

/**
 * DTO for {@link com.neobis.neoauth.entities.User}
 */
@Builder
public record JwtResponseDto(String username, String accessToken, String refreshToken, String error){
}