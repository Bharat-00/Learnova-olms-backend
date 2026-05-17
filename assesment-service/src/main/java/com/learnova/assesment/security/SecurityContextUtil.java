package com.learnova.assesment.security;

import com.learnova.assesment.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    private static final String DEFAULT_EMAIL = "student@learnova.local";
    private static final String DEFAULT_ROLE = "ADMIN";

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return DEFAULT_EMAIL;
        }
        return userEmail;
    }

    public void validateInstructorOrAdmin(String role) {
        String resolvedRole = resolveRole(role);
        if (!resolvedRole.equalsIgnoreCase("INSTRUCTOR") && !resolvedRole.equalsIgnoreCase("ADMIN")) {
            throw new ForbiddenException("Only instructors or admins can manage assessments");
        }
    }

    public boolean isAdmin(String role) {
        return resolveRole(role).equalsIgnoreCase("ADMIN");
    }

    private String resolveRole(String role) {
        if (role == null || role.isBlank()) {
            return DEFAULT_ROLE;
        }
        return role;
    }
}
