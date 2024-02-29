package com.neobis.neoauth.controller;


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

//
//    @PostMapping("/login")
//    public ResponseEntity<JwtResponseDto> authenticate(@RequestBody JwtRequestDto authRequest){
//        return authService.authentication(authRequest);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> authorize(@RequestBody UserRequestDto registrationUserDto){
//        return   authService.createNewUser(registrationUserDto);}



}
