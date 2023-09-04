package dev.valentino.whatsapp.chat.impl.direct;

import java.util.UUID;

public record DirectChatDTO(
        UUID chatId,
        UUID recipientId,
        String name,
        byte[] avatar,
        String latestMessageText
) {
}
