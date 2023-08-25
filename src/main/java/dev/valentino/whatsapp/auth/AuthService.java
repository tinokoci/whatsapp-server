package dev.valentino.whatsapp.auth;

import dev.valentino.whatsapp.auth.request.AuthRequest;
import dev.valentino.whatsapp.auth.response.AuthResponse;
import dev.valentino.whatsapp.user.exception.UserException;
import dev.valentino.whatsapp.user.UserRepository;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProvider authProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthResponse signUp(AuthRequest request) {
        String username = request.username();
        String fullName = request.fullName();
        String password = request.password();

        // Check if username is taken
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserException("Username is taken");
        }
        // Create user object from request and save it
        WapUser user = WapUser.builder()
                .username(username)
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);

        // Create authentication and set it in spring context
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Return auth response with jwt
        String jwt = JwtUtil.createJwt(authentication);
        return new AuthResponse(jwt);
    }

    public AuthResponse login(AuthRequest request) {
        String username = request.username();
        String password = request.password();

        // Try to authenticate user and password
        Authentication authentication = authProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Set authentication in spring context if valid
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Return auth response with jwt
        String jwt = JwtUtil.createJwt(authentication);
        return new AuthResponse(jwt);
    }
}
