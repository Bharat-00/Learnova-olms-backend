package com.learnova.assesment.security;

import com.learnova.assesment.exception.ForbiddenException;
import com.learnova.assesment.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new UnauthorizedException("User email is missing");
        }

        return userEmail;
    }

    public void validateInstructorOrAdmin(String role) {
        if (role == null || role.isBlank()) {
            throw new UnauthorizedException("User role is missing");
        }

        if (!role.equalsIgnoreCase("INSTRUCTOR") && !role.equalsIgnoreCase("ADMIN")) {
            throw new ForbiddenException("Only instructors or admins can manage assessments");
        }
    }

    public boolean isAdmin(String role) {
        return role != null && role.equalsIgnoreCase("ADMIN");
    }
}