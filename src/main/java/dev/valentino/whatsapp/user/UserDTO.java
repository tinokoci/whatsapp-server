package dev.valentino.whatsapp.user;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String fullName,
        byte[] avatar
) {
}
