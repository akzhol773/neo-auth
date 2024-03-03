package com.neobis.neoauth.service.Impl;

import com.neobis.neoauth.dtos.*;
import com.neobis.neoauth.entities.ConfirmationToken;
import com.neobis.neoauth.entities.Role;
import com.neobis.neoauth.entities.User;
import com.neobis.neoauth.exceptions.*;
import com.neobis.neoauth.repository.UserRepository;
import com.neobis.neoauth.service.ConfirmationTokenService;
import com.neobis.neoauth.service.EmailService;
import com.neobis.neoauth.service.RoleService;
import com.neobis.neoauth.service.UserService;
import com.neobis.neoauth.util.CustomUserDetails;
import com.neobis.neoauth.util.EmailTemplates;
import com.neobis.neoauth.util.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomUserDetails customUserDetails;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final EmailTemplates emailTemplates;

    @Override
    public ResponseEntity<UserResponseDto> createNewUser(UserRequestDto registrationUserDto) {

        if (userRepository.findByUsername(registrationUserDto.username()).isPresent()) {
            throw new UsernameAlreadyTakenException("Username is already taken. Please, try to use another one.");
        }
        if (userRepository.findByEmail(registrationUserDto.email()).isPresent()) {
            throw new EmailAlreadyExistException("Email already exist. Please, try to use another one.");
        }
        User user = new User();
        user.setEnabled(false);
        user.setEmail(registrationUserDto.email());
        user.setUsername(registrationUserDto.username());
        Role userRole = roleService.getUserRole()
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        user.setRoles(Collections.singletonList(userRole));
        String password = registrationUserDto.password();
        String confirmPassword = registrationUserDto.confirmPassword();
        if (!password.equals(confirmPassword)) {
            throw new PasswordDontMatchException("Passwords do not match.");
        }
        user.setPassword(passwordEncoder.encode(registrationUserDto.password()));
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                null,
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/auth/confirm?token=" + token;
        emailService.send(
                registrationUserDto.email(), emailTemplates.buildEmail(registrationUserDto.username(), link)
        );

        return ResponseEntity.ok(new UserResponseDto("Success! Please, check your email for the confirmation", user.getUsername()));
    }

    @Override
    public ResponseEntity<JwtResponseDto> authenticate(JwtRequestDto authRequest) {


        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
            UserDetails userDetails = customUserDetails.loadUserByUsername(authRequest.username());
            String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
            String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails);
            return ResponseEntity.ok(new JwtResponseDto(authRequest.username(), accessToken, refreshToken, null));

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialException("Invalid username or password");
        }



    }

    @Override
    public ResponseEntity<JwtRefreshTokenDto> refreshToken(String refreshToken) {

        try {
            if (refreshToken == null) {
                return ResponseEntity.badRequest().build();
            }

            String usernameFromRefreshToken = jwtTokenUtils.getUsernameFromRefreshToken(refreshToken);
            UserDetails userDetails = customUserDetails.loadUserByUsername(usernameFromRefreshToken);

            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String accessToken = jwtTokenUtils.generateAccessToken(userDetails);
            return ResponseEntity.ok(new JwtRefreshTokenDto(usernameFromRefreshToken, accessToken, null));

        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body( new JwtRefreshTokenDto(null,null, e.getMessage()));
        }
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(()->new TokenNotFoundException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException("Email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("Token has expired");

        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.getUser().setEnabled(true);

        return "Email successfully confirmed";
    }
}
