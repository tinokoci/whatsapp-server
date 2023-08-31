package dev.valentino.whatsapp.message.request;

import java.util.UUID;

public record MessageSendRequest(
        UUID entityId, // can be either used for Chat or WapUser
        String text
) {
}
