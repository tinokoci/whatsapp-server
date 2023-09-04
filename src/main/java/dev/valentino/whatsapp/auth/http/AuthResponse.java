package dev.valentino.whatsapp.auth.http;

import dev.valentino.whatsapp.user.UserDTO;

public record AuthResponse(UserDTO user) {
}
