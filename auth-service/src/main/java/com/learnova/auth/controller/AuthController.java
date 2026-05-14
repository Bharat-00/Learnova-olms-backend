package com.learnova.auth.controller;

import com.learnova.auth.dto.ApiResponse;
import com.learnova.auth.dto.AuthResponse;
import com.learnova.auth.dto.LoginRequest;
import com.learnova.auth.dto.RegisterRequest;
import com.learnova.auth.dto.UserInfoResponse;
import com.learnova.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "APIs for authentication and user identity")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        log.info("Register request received for email={}", request.getEmail());

        AuthResponse response = authService.register(request);

        return ResponseEntity.status(201)
                .body(
                        ApiResponse.<AuthResponse>builder()
                                .success(true)
                                .message("User registered successfully")
                                .data(response)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @PostMapping("/login")
    @Operation(summary = "Login user and generate JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Login request received for email={}", request.getEmail());

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/me")
    @Operation(summary = "Get current logged-in user details")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser(
            Authentication authentication
    ) {
        String email = authentication.getName();

        UserInfoResponse response = authService.getCurrentUser(email);

        return ResponseEntity.ok(
                ApiResponse.<UserInfoResponse>builder()
                        .success(true)
                        .message("Current user fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}