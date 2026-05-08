package com.learnova.user.service;

import com.learnova.user.dto.UserProfileRequest;
import com.learnova.user.dto.UserProfileResponse;
import com.learnova.user.entity.UserProfile;
import com.learnova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserProfileResponse getProfile(String email) {

        UserProfile user = repository.findByEmail(email)
                .orElseGet(() -> repository.save(
                        UserProfile.builder()
                                .email(email)
                                .name("New User")
                                .build()
                ));

        return mapToResponse(user);
    }

    public UserProfileResponse updateProfile(String email, UserProfileRequest request) {

        UserProfile user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setBio(request.getBio());
        user.setProfileImage(request.getProfileImage());

        repository.save(user);

        return mapToResponse(user);
    }

    private UserProfileResponse mapToResponse(UserProfile user) {

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .build();
    }
}