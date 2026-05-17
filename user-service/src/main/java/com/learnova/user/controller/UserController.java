package com.learnova.user.controller;

import com.learnova.user.dto.UserProfileRequest;
import com.learnova.user.dto.UserProfileResponse;
import com.learnova.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final String DEFAULT_EMAIL = "bharat@gmail.com";

    private final UserService service;

    @GetMapping({"/api/v1/users", "/api/v1/profiles"})
    public List<UserProfileResponse> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping({"/api/v1/users/me", "/api/v1/profiles/me"})
    public UserProfileResponse getMyProfile(
            @RequestHeader(value = "X-User-Email", required = false) String email) {
        return service.getProfile(resolveEmail(email));
    }

    @PutMapping({"/api/v1/users/me", "/api/v1/profiles/me"})
    public UserProfileResponse updateMyProfile(
            @RequestHeader(value = "X-User-Email", required = false) String email,
            @Valid @RequestBody UserProfileRequest request) {
        return service.updateProfile(resolveEmail(email), request);
    }

    @PostMapping({"/api/v1/users", "/api/v1/profiles"})
    public UserProfileResponse createProfile(@Valid @RequestBody UserProfileRequest request) {
        return service.createProfile(request);
    }

    @GetMapping({"/api/v1/users/{id}", "/api/v1/profiles/{id}"})
    public UserProfileResponse getUserById(@PathVariable("id") Long id) {
        return service.getProfileById(id);
    }

    @PutMapping({"/api/v1/users/{id}", "/api/v1/profiles/{id}"})
    public UserProfileResponse updateUserById(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserProfileRequest request) {
        return service.updateProfileById(id, request);
    }

    @GetMapping({"/api/v1/users/email/{email}", "/api/v1/profiles/email/{email}"})
    public UserProfileResponse getUserByEmail(@PathVariable("email") String email) {
        return service.getProfile(email);
    }

    @DeleteMapping({"/api/v1/users/{id}", "/api/v1/profiles/{id}"})
    public void deleteUser(@PathVariable("id") Long id) {
        service.deleteProfile(id);
    }

    private String resolveEmail(String email) {
        if (email == null || email.isBlank()) {
            return DEFAULT_EMAIL;
        }
        return email;
    }
}
