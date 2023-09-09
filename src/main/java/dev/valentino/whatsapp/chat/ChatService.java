package dev.valentino.whatsapp.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat getChatById(UUID id) throws ChatException {
        return chatRepository
                .findById(id)
                .orElseThrow(() -> new ChatException("Chat not found"));
    }
}
