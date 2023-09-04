package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.ChatException;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.message.exception.MessageException;
import dev.valentino.whatsapp.message.request.MessageSendRequest;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageSendRequest request) throws UserNotFoundException, ChatException {
        MessageDTO message = messageService.sendMessage(request);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<List<Message>> deleteMessage(@PathVariable UUID messageId) throws ChatException, UserNotFoundException, MessageException {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
