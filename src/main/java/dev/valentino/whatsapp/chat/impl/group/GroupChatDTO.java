package dev.valentino.whatsapp.chat.impl.group;

import java.util.UUID;

public record GroupChatDTO(
        UUID chatId,
        String name,
        byte[] avatar
) {
}
