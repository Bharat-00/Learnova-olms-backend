package com.learnova.payment.security;

import org.springframework.stereotype.Component;

import com.learnova.payment.exception.UnauthorizedException;

@Component
public class SecurityContextUtil {

    public String getUserEmail(String userEmail) {

        if (userEmail == null || userEmail.isBlank()) {
            throw new UnauthorizedException("User email is missing");
        }

        return userEmail;
    }
}