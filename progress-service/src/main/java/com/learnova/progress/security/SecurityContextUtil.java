package com.learnova.progress.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    private static final String DEFAULT_EMAIL = "bharat@gmail.com";

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return DEFAULT_EMAIL;
        }
        return userEmail;
    }

    public String getUserEmail(String userEmail, Long userId) {
        if (userEmail != null && !userEmail.isBlank()) {
            return userEmail;
        }
        if (userId != null) {
            return "user" + userId + "@learnova.local";
        }
        return DEFAULT_EMAIL;
    }
}
