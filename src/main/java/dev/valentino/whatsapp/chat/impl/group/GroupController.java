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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable UUID groupId) throws ChatException {
        GroupDTO group = groupService.getGroupById(groupId).toGroupDTO();
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getUserGroups() {
        List<GroupDTO> groups = groupService.getUserGroups();
        return ResponseEntity.ok(groups);
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupCreateRequest request) throws UserNotFoundException {
        GroupDTO group = groupService.createGroup(request).toGroupDTO();
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Chat> deleteGroup(@PathVariable UUID groupId) throws ChatException, UserNotFoundException {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{groupId}/add/{userToAddId}")
    public ResponseEntity<Void> addUserToGroup(@PathVariable UUID groupId, @PathVariable UUID userToAddId) throws ChatException, UserNotFoundException {
        groupService.addUserToGroup(userToAddId, groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{groupId}/remove/{userToRemoveId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable UUID groupId, @PathVariable UUID userToRemoveId) throws ChatException, UserNotFoundException {
        groupService.removeUserFromGroup(userToRemoveId, groupId);
        return ResponseEntity.ok().build();
    }
}
