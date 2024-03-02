package com.neobis.neoauth.controller;


import com.neobis.neoauth.dtos.*;
import com.neobis.neoauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth/")
public class AuthController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest){
       return  userService.authenticate(authRequest);

    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto registrationUserDto){
        return  userService.createNewUser(registrationUserDto);}

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshTokenDto> refreshToken(@RequestParam String refreshToken){
        return  userService.refreshToken(refreshToken);

    }

}
