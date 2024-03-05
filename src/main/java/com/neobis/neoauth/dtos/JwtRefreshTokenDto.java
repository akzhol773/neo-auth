package com.neobis.neoauth.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
public record JwtRefreshTokenDto(String username, String newAccessToken) {
}
