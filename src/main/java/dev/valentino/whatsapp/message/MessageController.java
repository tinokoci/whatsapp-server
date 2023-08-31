package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.message.exception.MessageException;
import dev.valentino.whatsapp.message.request.MessageSendRequest;
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

    @PostMapping("/personal")
    public ResponseEntity<MessageDTO> sendPersonalMessage(@RequestBody MessageSendRequest request) throws UserNotFoundException, ChatException {
        MessageDTO message = messageService.sendPersonalMessage(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/group")
    public ResponseEntity<Message> sendGroupMessage(@RequestBody MessageSendRequest request) throws UserNotFoundException, ChatException {
        Message message = messageService.sendGroupMessage(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/chat/{entityId}")
    public ResponseEntity<List<MessageDTO>> getChatMessages(@PathVariable UUID entityId) throws ChatException, UserNotFoundException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        List<MessageDTO> messages = messageService.getDirectChatMessages(entityId, user);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<List<Message>> deleteMessage(@PathVariable UUID messageId) throws ChatException, UserNotFoundException, MessageException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        messageService.deleteMessage(messageId, user);
        return ResponseEntity.ok().build();
    }
}
