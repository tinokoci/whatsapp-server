package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.chat.dto.ChatDTO;
import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.chat.request.GroupChatCreateRequest;
import dev.valentino.whatsapp.chat.request.NormalChatCreateRequest;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/normal")
    public ResponseEntity<ChatDTO> getOrCreateChat(@RequestBody NormalChatCreateRequest request) throws UserNotFoundException, ChatException {
        ChatDTO chat = chatService.getOrCreateChat(request.receiverId());
        return ResponseEntity.ok(chat);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroup(@RequestBody GroupChatCreateRequest request) throws UserNotFoundException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        Chat chat = chatService.createGroup(request, user);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatById(@PathVariable UUID chatId) throws ChatException {
        Chat chat = chatService.findChatById(chatId);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/user")
    public ResponseEntity<List<WapUser>> findAllChatsOfUser() throws UserNotFoundException {
        List<WapUser> chats = chatService.findAllChatsOfUser();
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroup(@PathVariable UUID chatId, @PathVariable UUID userId) throws ChatException, UserNotFoundException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        Chat chat = chatService.addUserToGroup(user, userId, chatId);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroup(@PathVariable UUID chatId, @PathVariable UUID userId) throws ChatException, UserNotFoundException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        Chat chat = chatService.removeUserFromGroup(user, userId, chatId);
        return ResponseEntity.ok(chat);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Chat> removeUserFromGroup(@PathVariable UUID chatId) throws ChatException, UserNotFoundException {
        WapUser user = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        chatService.deleteChat(chatId, user);
        return ResponseEntity.ok().build();
    }
}
