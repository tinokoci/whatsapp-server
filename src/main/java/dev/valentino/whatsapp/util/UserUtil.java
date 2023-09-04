package dev.valentino.whatsapp.util;

import dev.valentino.whatsapp.auth.AuthToken;
import dev.valentino.whatsapp.user.WapUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class UserUtil {

    public static final SimpleGrantedAuthority DEFAULT_AUTHORITY = new SimpleGrantedAuthority("USER");

    // DANGEROUS!
    // Only use if needed for ID reference for other queries,
    // otherwise fetch user from the database for up-to-date fields
    public static WapUser getUserFromContext() {
        return getAuthenticationFromContext().getUser();
    }

    public static UUID getIdFromContext() {
        return getAuthenticationFromContext().getUser().getId();
    }

    public static String getUsernameFromContext() {
        return getAuthenticationFromContext().getUser().getUsername();
    }

    public static AuthToken getAuthenticationFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AuthToken)) {
            throw new IllegalArgumentException("Authentication object is not an instance of AuthToken");
        }
        return (AuthToken) authentication;
    }
}
