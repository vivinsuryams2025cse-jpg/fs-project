package com.skillbridge.util;

import com.skillbridge.exception.UnauthorizedException;
import com.skillbridge.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new UnauthorizedException("User is not authenticated");
        }
        return userDetails;
    }

    public static Long getCurrentStudentId() {
        return getCurrentUserDetails().getId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUserDetails().getUsername();
    }
}
