package dev.valentino.whatsapp.auth;

import dev.valentino.whatsapp.user.WapUser;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class AuthToken extends UsernamePasswordAuthenticationToken {

    private final WapUser user;

    public AuthToken(WapUser user, Object credentials) {
        super(user.getUsername(), credentials);
        this.user = user;
    }

    public AuthToken(WapUser user, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), credentials, authorities);
        this.user = user;
    }
}