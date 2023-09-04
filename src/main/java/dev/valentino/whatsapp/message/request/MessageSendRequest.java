package dev.valentino.whatsapp.message.request;

import java.util.UUID;

public record MessageSendRequest(
        UUID chatId,
        String text
) {
}
