package com.learnova.course.security;

import com.learnova.course.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    public String getUserEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UnauthorizedException("User email is missing");
        }

        return email;
    }

    public String getUserRole(String role) {
        if (role == null || role.isBlank()) {
            throw new UnauthorizedException("User role is missing");
        }

        return role;
    }

    public boolean isAdmin(String role) {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isInstructor(String role) {
        return "INSTRUCTOR".equalsIgnoreCase(role);
    }
}