package dev.valentino.whatsapp.user;

public record UserUpdateRequest(
        String username,
        String fullName
) {
}
