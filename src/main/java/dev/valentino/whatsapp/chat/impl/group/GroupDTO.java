package dev.valentino.whatsapp.chat.impl.group;

import java.util.UUID;

public record GroupDTO(
        UUID chatId,
        String name,
        byte[] avatar
) {
}
