package org.voting.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    public static User safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof User) ? (User) principal : null;
    }

    public static User get() {
        User user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    private SecurityUtil() {
    }

    public static String getAuthUsername() {
        return get().getUsername();
    }
}

