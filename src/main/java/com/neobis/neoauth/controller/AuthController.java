package com.neobis.neoauth.controller;


import com.neobis.neoauth.dtos.*;
import com.neobis.neoauth.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto registrationUserDto){
        return  userService.createNewUser(registrationUserDto);}


    @Operation(
            summary = "Refresh the token",
            description = "If the token is expired then it is possible to generate a new access token using refresh token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully returned a new access token"),

    })

    @Hidden
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshTokenDto> refreshToken(@RequestBody NewAccessTokenRequest newAccessTokenRequest){
         return  userService.refreshToken(newAccessTokenRequest);

    }

    @Operation(
            summary = "Confirm the email using this api",
            description = "Whenever a user is registered he or she gets email containing link to activate his or her account"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email successfully confirmed"),

    })
    @Hidden
    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirm(@RequestParam("token") String token){
        return userService.confirmEmail(token);
    }

    @PostMapping("/re-confirm-email")
    public ResponseEntity<String> reconfirm(@RequestBody UsernameEmailDto usernameEmailDto) {
        return  userService.resendConfirmation(usernameEmailDto);

    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPassworDto dto){
        return userService.forgotPassword(dto);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam ("resetToken") String resetToken, @RequestBody ResetPasswordDto resetPasswordDto){
        return userService.resetPassword(resetToken, resetPasswordDto);
    }






}
