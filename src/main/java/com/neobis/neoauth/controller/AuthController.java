package com.neobis.neoauth.controller;


import com.neobis.neoauth.dtos.JwtRequestDto;
import com.neobis.neoauth.dtos.UserRequestDto;
import com.neobis.neoauth.dtos.UserResponseDto;
import com.neobis.neoauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth/")
public class AuthController {

    private final UserService userService;


//    @PostMapping("/login")
//    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto authRequest){
//        return authService.authentication(authRequest);
//    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto registrationUserDto){
        return   userService.createNewUser(registrationUserDto);}



}
