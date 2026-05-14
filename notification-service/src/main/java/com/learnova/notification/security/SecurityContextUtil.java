package com.learnova.notification.security;

import com.learnova.notification.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    public String getUserEmail(String userEmail) {

        if (userEmail == null || userEmail.isBlank()) {
            throw new UnauthorizedException("User email is missing");
        }

        return userEmail;
    }
}