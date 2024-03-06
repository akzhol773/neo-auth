package com.neobis.neoauth.controller;


import com.neobis.neoauth.dtos.*;
import com.neobis.neoauth.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "200", description = "Successfully returned a token"),
            @ApiResponse(responseCode = "403", description = "Username or password is invalid"),
            @ApiResponse(responseCode = "403", description = "Username is enabled")
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
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "The provided username is already taken"),
            @ApiResponse(responseCode = "409", description = "The provided email is already taken")
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
            @ApiResponse(responseCode = "403", description = "Token has expired"),
            @ApiResponse(responseCode = "403", description = "Token not found"),

    })

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshTokenDto> refreshToken(@RequestParam("refreshToken") String refreshToken){
         return  userService.refreshToken(refreshToken);

    }

    @Operation(
            summary = "Confirm the email",
            description = "Whenever a user is registered he or she gets email containing link to activate his or her account"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email successfully confirmed"),
            @ApiResponse(responseCode = "403", description = "Token has expired"),
            @ApiResponse(responseCode = "403", description = "Token not found")


    })
    @Hidden
    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirm(@RequestParam("token") String token){
        return userService.confirmEmail(token);
    }


    @Operation(
            summary = "Reconfirm the email",
            description = "User can get another link to confirm their email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email successfully confirmed"),
            @ApiResponse(responseCode = "403", description = "Token has expired"),
            @ApiResponse(responseCode = "403", description = "Token not found")

    })
    @PostMapping("/re-confirm-email")
    public ResponseEntity<String> reconfirm(@RequestBody ReconfirmEmailDto dto) {
        return  userService.resendConfirmation(dto);

    }



    @Operation(
            summary = "Users can reset their password using this link. It sends link to the email",
            description = "User can get another link to confirm their email"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Link has been sent to the email"),
            @ApiResponse(responseCode = "403", description = "User not found"),

    })
    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto dto){
        return userService.forgotPassword(dto);
    }

    @Operation(
            summary = "Reset password",
            description = "Resetting the password with the provided new password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password has been changed successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid token"),

    })
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam ("resetToken") String resetToken, @RequestBody ResetPasswordDto resetPasswordDto){
        return userService.resetPassword(resetToken, resetPasswordDto);
    }

}
