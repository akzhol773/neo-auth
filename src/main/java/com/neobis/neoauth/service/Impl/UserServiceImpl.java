package com.neobis.neoauth.service.Impl;

import com.neobis.neoauth.dtos.*;
import com.neobis.neoauth.entities.ConfirmationToken;
import com.neobis.neoauth.entities.Role;
import com.neobis.neoauth.entities.User;
import com.neobis.neoauth.exceptions.*;
import com.neobis.neoauth.repository.ConfirmationTokenRepository;
import com.neobis.neoauth.repository.UserRepository;
import com.neobis.neoauth.service.ConfirmationTokenService;
import com.neobis.neoauth.service.EmailService;
import com.neobis.neoauth.service.RoleService;
import com.neobis.neoauth.service.UserService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final EmailTemplates emailTemplates;
    private final ConfirmationTokenRepository confirmationTokenRepository;

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

        ConfirmationToken confirmationToken = generateConfirmToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "https://royal-nerve-lorby.up.railway.app/api/auth/confirm?token=" + confirmationToken.getToken();
        sendConfirmationMail(link, user);

        return ResponseEntity.ok(new UserResponseDto("Success! Please, check your email for the confirmation", user.getUsername()));
    }




    @Override
    public ResponseEntity<JwtResponseDto> authenticate(JwtRequestDto authRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenUtils.generateAccessToken(user);
            String refreshToken = jwtTokenUtils.generateRefreshToken(user);
            return ResponseEntity.ok(new JwtResponseDto(authRequest.username(), accessToken, refreshToken, null));


        } catch (AuthenticationException exception) {
            if (exception instanceof DisabledException) {
                throw new DisabledException("User is not enabled yet");
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        }
    }

    @Override
    public ResponseEntity<JwtRefreshTokenDto> refreshToken(String refreshToken) {

        try {
            if (refreshToken == null) {
                return ResponseEntity.badRequest().build();
            }

            String usernameFromRefreshToken = jwtTokenUtils.getUsernameFromRefreshToken(refreshToken);
            User user = userRepository.findByUsername(usernameFromRefreshToken).orElseThrow();

            if (usernameFromRefreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String accessToken = jwtTokenUtils.generateAccessToken(user);
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
    public ResponseEntity<String> confirmEmail(String token) {
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

        return ResponseEntity.ok().body("Email successfully confirmed. Go back to your login page");
    }

    @Override
    public ConfirmationToken generateConfirmToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                null,
                user);
        return confirmationToken;
    }

    @Override
    public ResponseEntity<String> resendConfirmation(UsernameEmailDto usernameEmailDto) {
        User user = userRepository.findByUsernameAndEmail(
                usernameEmailDto.username(), usernameEmailDto.email()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        if(user.isEnabled()){
            throw new UserConfirmedException("Email already confirmed");
        }

        List<ConfirmationToken> confirmationTokens = confirmationTokenRepository.findByUser(user);
        for(ConfirmationToken confirmationToken : confirmationTokens){
            confirmationToken.setToken(null);
            confirmationTokenRepository.save(confirmationToken);
        }


        ConfirmationToken newConfirmationToken = generateConfirmToken(user);
        String link = "https://royal-nerve-lorby.up.railway.app/api/auth/confirm?token=" + newConfirmationToken.getToken();
        sendConfirmationMail(link, user);
        return ResponseEntity.ok("Success! Please, check your email for the re-confirmation");
    }

    @Override
    public void sendConfirmationMail(String link, User user){
        emailService.send(user.getEmail(), emailTemplates.buildEmail(user.getUsername(), link));
    }
}
