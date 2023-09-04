package dev.valentino.whatsapp.auth.http;

public record AuthRequest(
        String username,
        String fullName,
        String password
) {
}
