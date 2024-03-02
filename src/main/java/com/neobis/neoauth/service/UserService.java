package com.neobis.neoauth.service;

import com.neobis.neoauth.dtos.JwtRequestDto;
import com.neobis.neoauth.dtos.JwtResponseDto;
import com.neobis.neoauth.dtos.UserRequestDto;
import com.neobis.neoauth.dtos.UserResponseDto;
import org.springframework.http.ResponseEntity;



public interface UserService {
    ResponseEntity<UserResponseDto> createNewUser(UserRequestDto registrationUserDto);

    ResponseEntity<JwtResponseDto> authenticate(JwtRequestDto authRequest);
}
