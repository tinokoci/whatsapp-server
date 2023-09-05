package dev.valentino.whatsapp.message.dto;

public record MessageDTO(
        String chatId,
        String senderId,
        String text,
        Long timestamp
) {
}
