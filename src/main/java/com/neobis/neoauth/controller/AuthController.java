package com.neobis.neoauth.controller;


import com.neobis.neoauth.dtos.*;
import com.neobis.neoauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Login",
            description = "Endpoint for getting tokens after login"

    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully returned a token"),
            @ApiResponse(responseCode = "409", description = "Username or password is invalid", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest){
       return  userService.authenticate(authRequest);

    }

    @Operation(
            summary = "Registration",
            description = "Endpoint for customer to register a new account. Requires a body"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer successfully registered"),
            @ApiResponse(responseCode = "403", description = "The provided username is already taken", content = @Content)
    })

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto registrationUserDto){
        return  userService.createNewUser(registrationUserDto);}

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshTokenDto> refreshToken(@RequestParam String refreshToken){
        return  userService.refreshToken(refreshToken);

    }

}
