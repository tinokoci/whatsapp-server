package dev.valentino.whatsapp.auth;

import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final BadCredentialsException BAD_CREDENTIALS = new BadCredentialsException("Bad credentials");

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object credentials = authentication.getCredentials();

        if (!(credentials instanceof String)) {
            throw BAD_CREDENTIALS;
        }
        String password = (String) authentication.getCredentials();
        WapUser user = userService.getUserByUsername(authentication.getName());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw BAD_CREDENTIALS;
        }
        authentication = new AuthToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(AuthToken.class);
    }
}
