package com.neobis.neoauth.service.Impl;

import com.neobis.neoauth.dtos.UserRequestDto;
import com.neobis.neoauth.dtos.UserResponseDto;
import com.neobis.neoauth.entities.Role;
import com.neobis.neoauth.entities.User;
import com.neobis.neoauth.exceptions.PasswordDontMatchException;
import com.neobis.neoauth.exceptions.UsernameAlreadyTakenException;
import com.neobis.neoauth.repository.RoleRepository;
import com.neobis.neoauth.repository.UserRepository;
import com.neobis.neoauth.service.RoleService;
import com.neobis.neoauth.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<UserResponseDto> createNewUser(UserRequestDto registrationUserDto) {
        if(userRepository.findByUsername(registrationUserDto.username()).isPresent()){
            throw new UsernameAlreadyTakenException("Username is already taken. Please, try to use another one.");
        }
        User user = new User();
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
        return ResponseEntity.ok(new UserResponseDto(user.getUsername()));
    }
}
