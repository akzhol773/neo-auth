package com.neobis.neoauth.service;

import com.neobis.neoauth.dtos.*;
import com.neobis.neoauth.entities.ConfirmationToken;
import com.neobis.neoauth.entities.User;
import org.springframework.http.ResponseEntity;



public interface UserService {
    ResponseEntity<UserResponseDto> createNewUser(UserRequestDto registrationUserDto);

    ResponseEntity<JwtResponseDto> authenticate(JwtRequestDto authRequest);

    ResponseEntity<JwtRefreshTokenDto> refreshToken(String token);

    ResponseEntity<String> confirmEmail(String token);

    ConfirmationToken generateConfirmToken(User user);

    ResponseEntity<String> resendConfirmation(UsernameEmailDto usernameEmailDto);
    public void sendConfirmationMail(String link, User user);
}
