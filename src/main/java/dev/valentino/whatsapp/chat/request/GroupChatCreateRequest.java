package dev.valentino.whatsapp.chat.request;

import java.util.List;
import java.util.UUID;

public record GroupChatCreateRequest(
        String chatName,
        String chatImage,
        List<UUID> participants
) {
}
