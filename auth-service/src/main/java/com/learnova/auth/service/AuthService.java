package com.learnova.auth.service;

import com.learnova.auth.dto.AuthResponse;
import com.learnova.auth.dto.LoginRequest;
import com.learnova.auth.dto.RegisterRequest;
import com.learnova.auth.dto.UserInfoResponse;
import com.learnova.auth.entity.User;
import com.learnova.auth.enums.Role;
import com.learnova.auth.exception.BadRequestException;
import com.learnova.auth.exception.ResourceNotFoundException;
import com.learnova.auth.exception.UnauthorizedException;
import com.learnova.auth.repository.UserRepository;
import com.learnova.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        log.info("Registering new user with email={}", email);

        if (userRepository.existsByEmail(email)) {
            log.warn("Registration failed. Email already exists={}", email);
            throw new BadRequestException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(
                savedUser.getEmail(),
                savedUser.getRole().name()
        );

        log.info("User registered successfully with id={}", savedUser.getId());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().toLowerCase().trim();

        log.info("Login attempt for email={}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed. Email not found={}", email);
                    return new UnauthorizedException("Invalid email or password");
                });

        boolean isPasswordValid =
                passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isPasswordValid) {
            log.warn("Login failed. Invalid password for email={}", email);
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        log.info("Login successful for email={}", email);

        return new AuthResponse(token);
    }

    public UserInfoResponse getCurrentUser(String email) {

        log.info("Fetching current user for email={}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}