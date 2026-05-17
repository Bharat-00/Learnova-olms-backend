package com.learnova.discussion.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return "anonymous@learnova.com";
        }
        return userEmail;
    }
}
