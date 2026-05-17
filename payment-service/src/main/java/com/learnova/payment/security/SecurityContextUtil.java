package com.learnova.payment.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    private static final String DEFAULT_TEST_EMAIL = "bharat@gmail.com";

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return DEFAULT_TEST_EMAIL;
        }
        return userEmail;
    }
}
