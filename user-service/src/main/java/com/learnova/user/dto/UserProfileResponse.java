package com.learnova.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String email;
    private String name;
    private String bio;
    private String profileImage;
}