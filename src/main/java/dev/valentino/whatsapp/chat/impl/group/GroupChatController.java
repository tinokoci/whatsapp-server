package dev.valentino.whatsapp.chat.impl.group;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.chat.ChatException;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats/groups")
@RequiredArgsConstructor
public class GroupChatController {

    private final GroupChatService groupChatService;

    @PostMapping
    public ResponseEntity<Chat> createGroupChat(@RequestBody GroupChatCreateRequest request) throws UserNotFoundException {
        Chat group = groupChatService.createGroup(request);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Chat> getGroupChatById(@PathVariable UUID groupId) throws ChatException {
        Chat group = groupChatService.getGroupChatById(groupId);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{groupId}/add/{userToAddId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable UUID groupId, @PathVariable UUID userToAddId) throws ChatException, UserNotFoundException {
        groupChatService.addUserToGroup(userToAddId, groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}/remove/{userToRemoveId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable UUID groupId, @PathVariable UUID userToRemoveId) throws ChatException, UserNotFoundException {
        groupChatService.removeUserFromGroup(userToRemoveId, groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Chat> deleteChat(@PathVariable UUID groupId) throws ChatException, UserNotFoundException {
        groupChatService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }
}
