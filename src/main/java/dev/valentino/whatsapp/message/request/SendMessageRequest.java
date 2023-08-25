package dev.valentino.whatsapp.message.request;

import java.util.UUID;

public record SendMessageRequest(
        UUID senderId,
        UUID chatId,
        String content
) {
}
