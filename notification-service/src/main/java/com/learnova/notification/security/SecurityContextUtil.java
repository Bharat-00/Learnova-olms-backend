package com.learnova.notification.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    private static final String FALLBACK_EMAIL = "bharat@gmail.com";

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return FALLBACK_EMAIL;
        }
        return userEmail;
    }
}
