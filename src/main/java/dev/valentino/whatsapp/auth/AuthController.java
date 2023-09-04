package dev.valentino.whatsapp.auth;

import dev.valentino.whatsapp.auth.http.AuthRequest;
import dev.valentino.whatsapp.auth.http.AuthResponse;
import dev.valentino.whatsapp.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody AuthRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return authService.logout();
    }
}
