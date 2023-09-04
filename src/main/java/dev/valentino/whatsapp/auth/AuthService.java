package dev.valentino.whatsapp.auth;

import dev.valentino.whatsapp.auth.http.AuthRequest;
import dev.valentino.whatsapp.auth.http.AuthResponse;
import dev.valentino.whatsapp.user.UserDTO;
import dev.valentino.whatsapp.user.UserRepository;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserFieldInUseException;
import dev.valentino.whatsapp.util.JwtUtil;
import dev.valentino.whatsapp.util.UserUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProvider authProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ResponseEntity<UserDTO> signUp(AuthRequest request) {
        String username = request.username();
        String fullName = request.fullName();
        String password = request.password();

        // Check if username is taken
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserFieldInUseException("Username is taken");
        }
        // Create user object from request and save it
        WapUser user = WapUser.builder()
                .username(username)
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);

        // Create authentication and set it in security context
        AuthToken authToken = new AuthToken(user, password);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        String jwt = JwtUtil.createJwt(authToken);
        return createAuthResponseEntity(jwt, authToken.getUser());
    }

    public ResponseEntity<UserDTO> login(AuthRequest request) {
        String username = request.username();
        String password = request.password();

        // Try to authenticate user and password
        AuthToken authToken = (AuthToken) authProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Set authentication in spring context if valid
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Re-assign to get latest object created by AuthProvider
        authToken = UserUtil.getAuthenticationFromContext();

        // Return auth response with jwt
        String jwt = JwtUtil.createJwt(authToken);
        return createAuthResponseEntity(jwt, authToken.getUser());
    }

    public ResponseEntity<Void> logout() {
        ResponseCookie jwtCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity
                .ok()
                .headers(consumer -> consumer.add(HttpHeaders.SET_COOKIE, jwtCookie.toString()))
                .build();
    }


    private ResponseEntity<UserDTO> createAuthResponseEntity(String jwt, WapUser user) {
        ResponseCookie jwtCookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();
        return ResponseEntity
                .ok()
                .headers(consumer -> consumer.add(HttpHeaders.SET_COOKIE, jwtCookie.toString()))
                .body(user.toDTO());
    }
}
