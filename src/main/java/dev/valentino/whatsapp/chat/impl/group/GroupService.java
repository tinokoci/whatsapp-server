package dev.valentino.whatsapp.chat.impl.group;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.chat.ChatActionAccessException;
import dev.valentino.whatsapp.chat.ChatException;
import dev.valentino.whatsapp.chat.ChatRepository;
import dev.valentino.whatsapp.chat.ChatType;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public Chat getGroupById(UUID groupId) throws ChatException {
        return chatRepository
                .findById(groupId, ChatType.GROUP)
                .orElseThrow(() -> new ChatException("Group not found"));
    }

    public List<GroupDTO> getUserGroups() {
        return chatRepository.findAllChatsByUser(UserUtil.getUserFromContext(), ChatType.GROUP)
                .stream()
                .map(Chat::toGroupDTO)
                .toList();
    }

    public Chat createGroup(GroupCreateRequest request) {
        return Chat.builder()
                .type(ChatType.GROUP)
                .participants(request.participants()
                        .stream()
                        .map(participantId -> {
                            try {
                                return userService.getUserById(participantId);
                            } catch (UserNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }).collect(Collectors.toSet()))
                .admins(Set.of(UserUtil.getUserFromContext()))
                .build();
    }

    public void addUserToGroup(UUID groupId, UUID userToAddId) throws UserNotFoundException, ChatException {
        Chat group = getGroupById(groupId);

        if (!group.isAdmin(UserUtil.getUserFromContext())) {
            throw new ChatActionAccessException();
        }
        WapUser userToAdd = userService.getUserById(userToAddId);

        if (group.isParticipant(userToAdd)) {
            throw new ChatException("User already in group");
        }
        group.getParticipants().add(userToAdd);
        chatRepository.save(group);
    }

    public void removeUserFromGroup(UUID groupId, UUID userToRemoveId) throws UserNotFoundException, ChatException {
        Chat group = getGroupById(groupId);

        if (!group.isAdmin(UserUtil.getUserFromContext())) {
            throw new ChatActionAccessException();
        }
        WapUser userToRemove = userService.getUserById(userToRemoveId);

        if (!group.isParticipant(userToRemove)) {
            throw new ChatException("User not in group");
        }
        group.getParticipants().remove(userToRemove);
        chatRepository.save(group);
    }

    public void renameGroup(UUID groupId, String name) throws ChatException {
        Chat group = getGroupById(groupId);

        if (!group.isAdmin(UserUtil.getUserFromContext())) {
            throw new ChatActionAccessException();
        }
        group.setName(name);
        chatRepository.save(group);
    }

    public void deleteGroup(UUID groupId) throws ChatException {
        Chat group = getGroupById(groupId);

        if (!group.isAdmin(UserUtil.getUserFromContext())) {
            throw new ChatActionAccessException();
        }
        chatRepository.deleteById(groupId);
    }
}
