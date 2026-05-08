package com.learnova.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String bio;

    private String profileImage;
}