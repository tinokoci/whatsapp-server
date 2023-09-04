package dev.valentino.whatsapp.chat.impl.group;

import java.util.List;
import java.util.UUID;

public record GroupChatCreateRequest(
        String name,
        String avatar,
        List<UUID> participants
) {
}
