package dev.valentino.whatsapp.auth.request;

public record AuthRequest(
        String username,
        String fullName,
        String password
) {
}
