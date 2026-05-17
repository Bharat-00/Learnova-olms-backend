package com.learnova.user.service;

import com.learnova.user.dto.UserProfileRequest;
import com.learnova.user.dto.UserProfileResponse;
import com.learnova.user.entity.UserProfile;
import com.learnova.user.exception.ResourceNotFoundException;
import com.learnova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<UserProfileResponse> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserProfileResponse getProfile(String email) {
        UserProfile user = repository.findByEmail(email)
                .orElseGet(() -> repository.save(
                        UserProfile.builder()
                                .email(email)
                                .name(extractNameFromEmail(email))
                                .build()
                ));

        return mapToResponse(user);
    }

    public UserProfileResponse getProfileById(Long id) {
        UserProfile user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found with id: " + id));

        return mapToResponse(user);
    }

    public UserProfileResponse createProfile(UserProfileRequest request) {
        String email = request.getEmail();

        if (email == null || email.isBlank()) {
            email = "user" + System.currentTimeMillis() + "@learnova.local";
        }

        final String finalEmail = email;

        UserProfile user = repository.findByEmail(finalEmail)
                .orElseGet(() -> UserProfile.builder().email(finalEmail).build());

        applyRequest(user, request);

        return mapToResponse(repository.save(user));
    }

    public UserProfileResponse updateProfile(String email, UserProfileRequest request) {
        UserProfile user = repository.findByEmail(email)
                .orElseGet(() -> UserProfile.builder().email(email).build());

        applyRequest(user, request);

        return mapToResponse(repository.save(user));
    }

    public UserProfileResponse updateProfileById(Long id, UserProfileRequest request) {
        UserProfile user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found with id: " + id));

        applyRequest(user, request);

        return mapToResponse(repository.save(user));
    }

    public void deleteProfile(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User profile not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private void applyRequest(UserProfile user, UserProfileRequest request) {
        user.setName(request.getName());
        user.setBio(request.getBio());
        user.setProfileImage(request.getProfileImage());

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
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

    private String extractNameFromEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            return "Learnova User";
        }
        return email.substring(0, email.indexOf('@'));
    }
}
