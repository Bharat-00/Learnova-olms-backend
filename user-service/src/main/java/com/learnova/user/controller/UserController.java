package com.learnova.user.controller;

import com.learnova.user.dto.UserProfileRequest;
import com.learnova.user.dto.UserProfileResponse;
import com.learnova.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/me")
    public UserProfileResponse getProfile(
            @RequestHeader("X-User-Email") String email) {

        return service.getProfile(email);
    }

    @PutMapping("/me")
    public UserProfileResponse updateProfile(
            @RequestHeader("X-User-Email") String email,
            @Valid @RequestBody UserProfileRequest request) {

        return service.updateProfile(email, request);
    }
}