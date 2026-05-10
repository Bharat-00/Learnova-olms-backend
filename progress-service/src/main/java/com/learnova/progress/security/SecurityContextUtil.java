package com.learnova.progress.security;

import com.learnova.progress.exception.UnauthorizedException;
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