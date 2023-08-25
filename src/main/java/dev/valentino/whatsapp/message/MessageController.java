package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.message.exception.MessageException;
import dev.valentino.whatsapp.message.request.SendMessageRequest;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequest request) throws UserNotFoundException, ChatException {
        Message message = messageService.sendMessage(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable UUID chatId) throws ChatException, UserNotFoundException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        List<Message> messages = messageService.getChatMessages(chatId, user);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<List<Message>> deleteMessage(@PathVariable UUID messageId) throws ChatException, UserNotFoundException, MessageException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        messageService.deleteMessage(messageId, user);
        return ResponseEntity.ok().build();
    }
}
