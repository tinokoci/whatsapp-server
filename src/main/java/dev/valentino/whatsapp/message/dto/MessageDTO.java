package dev.valentino.whatsapp.message.dto;

public record MessageDTO(
        String senderId,
        String text,
        Long timestamp
) {
}
