package dev.valentino.whatsapp.auth;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;
@Getter
public class AuthToken extends UsernamePasswordAuthenticationToken {

    private final UUID id;

    public AuthToken(UUID id, String username, Object credentials) {
        super(username, credentials);
        this.id = id;
    }

    public AuthToken(UUID id, String username, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(username, credentials, authorities);
        this.id = id;
    }

    public String getUsername() {
        return (String) getPrincipal();
    }
}
