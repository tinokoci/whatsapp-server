package dev.valentino.whatsapp.chat.impl.direct;

import dev.valentino.whatsapp.message.dto.MessageDTO;

import java.util.UUID;

public record DirectChatDTO(
        UUID chatId,
        UUID recipientId,
        String name,
        byte[] avatar,
        MessageDTO latestMessage
) {
}
