package dev.valentino.whatsapp.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    public static final SimpleGrantedAuthority DEFAULT_AUTHORITY = new SimpleGrantedAuthority("USER");

    public static String getUsernameFromContext() {
        Authentication authentication = getAuthenticationFromContext();
        if (authentication == null) return null;
        return authentication.getName();
    }

    public static Authentication getAuthenticationFromContext() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
