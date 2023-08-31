package dev.valentino.whatsapp.auth.response;

import java.util.UUID;

public record AuthResponse(String jwt, String username, UUID id) {
}
