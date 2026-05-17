package com.learnova.enrollment.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    private static final String DEFAULT_TEST_USER_EMAIL = "student@learnova.local";

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return DEFAULT_TEST_USER_EMAIL;
        }
        return userEmail.trim();
    }
}
