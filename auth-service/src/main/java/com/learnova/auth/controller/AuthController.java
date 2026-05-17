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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Auth Controller",
        description = "Authentication APIs"
)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        log.info(
                "Register request received for email={}",
                request.getEmail()
        );

        AuthResponse response =
                authService.register(request);

        ApiResponse<AuthResponse> apiResponse =
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        log.info(
                "Login request received for email={}",
                request.getEmail()
        );

        AuthResponse response =
                authService.login(request);

        ApiResponse<AuthResponse> apiResponse =
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser(
            Authentication authentication
    ) {

        String email = authentication.getName();

        UserInfoResponse response =
                authService.getCurrentUser(email);

        ApiResponse<UserInfoResponse> apiResponse =
                ApiResponse.<UserInfoResponse>builder()
                        .success(true)
                        .message("Current user fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(apiResponse);
    }
}