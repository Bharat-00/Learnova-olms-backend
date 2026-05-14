package com.learnova.lesson.security;

import com.learnova.lesson.exception.*;

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

        if (!role.equalsIgnoreCase("INSTRUCTOR")
                && !role.equalsIgnoreCase("ADMIN")) {

            throw new ForbiddenException(
                    "Only instructors or admins can manage lessons"
            );
        }
    }
}