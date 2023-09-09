package dev.valentino.whatsapp.chat.impl.direct;

import dev.valentino.whatsapp.chat.ChatException;
import dev.valentino.whatsapp.message.Message;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats/direct")
@RequiredArgsConstructor
public class DirectChatController {

    private final DirectChatService directChatService;

    @GetMapping("/{recipientId}")
    public ResponseEntity<DirectChatDTO> getOrCreateDirectChat(@PathVariable UUID recipientId) throws UserNotFoundException, ChatException {
        DirectChatDTO chat = directChatService.getOrCreateDirectChat(recipientId);
        return ResponseEntity.ok(chat);
    }

    @GetMapping
    public ResponseEntity<List<DirectChatDTO>> getUserDirectChats() throws UserNotFoundException {
        List<DirectChatDTO> chats = directChatService.getUserDirectChats();
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<List<DirectChatDTO>> searchDirectChatsByUsername(@PathVariable String username) {
        List<DirectChatDTO> chats = directChatService.searchDirectChatsByUsername(username);
        return ResponseEntity.ok(chats);
    }
}
