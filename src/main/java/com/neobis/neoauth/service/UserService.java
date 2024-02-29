package com.neobis.neoauth.service;

import com.neobis.neoauth.dtos.UserRequestDto;
import com.neobis.neoauth.dtos.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public interface UserService {
    ResponseEntity<UserResponseDto> createNewUser(UserRequestDto registrationUserDto);
}
