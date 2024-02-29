package com.neobis.neoauth.dtos;


import lombok.Data;

/**
 * DTO for {@link com.neobis.neoauth.entities.User}
 */
@Data
public record UserResponseDto(String username, String accessToken, String refreshToken){
}