package com.learnova.lesson.security;

import com.learnova.lesson.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    private static final String DEFAULT_EMAIL = "frontend-user@learnova.com";
    private static final String DEFAULT_ROLE = "INSTRUCTOR";

    public String getUserEmail(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            return DEFAULT_EMAIL;
        }
        return userEmail;
    }

    public String resolveRole(String role) {
        if (role == null || role.isBlank()) {
            return DEFAULT_ROLE;
        }
        return role;
    }

    public void validateInstructorOrAdmin(String role) {
        String resolvedRole = resolveRole(role);

        if (!resolvedRole.equalsIgnoreCase("INSTRUCTOR")
                && !resolvedRole.equalsIgnoreCase("ADMIN")) {
            throw new ForbiddenException("Only instructors or admins can manage lessons");
        }
    }
}
