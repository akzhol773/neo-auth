package com.neobis.neoauth.service;

import com.neobis.neoauth.dtos.*;
import org.springframework.http.ResponseEntity;



public interface UserService {
    ResponseEntity<UserResponseDto> createNewUser(UserRequestDto registrationUserDto);

    ResponseEntity<JwtResponseDto> authenticate(JwtRequestDto authRequest);

    ResponseEntity<JwtRefreshTokenDto> refreshToken(String token);
}
