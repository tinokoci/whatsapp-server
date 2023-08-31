package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.chat.dto.ChatDTO;
import dev.valentino.whatsapp.chat.exception.ChatActionAccessException;
import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.chat.request.GroupChatCreateRequest;
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
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public Chat findChatById(UUID chatId) throws ChatException {
        return chatRepository
                .findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found"));
    }

    public Chat findDirectChat(WapUser user1, WapUser user2) throws ChatException {
        return chatRepository
                .findDirectChat(user1, user2)
                .orElseThrow(() -> new ChatException("Chat not found"));
    }

    public List<ChatDTO> findAllChatsByUsername(String username) {
        return chatRepository.findAllByUserId(username)
                .stream()
                .map(Chat::toDTO)
                .collect(Collectors.toList());
    }

    // TODO: 8/31/23 refactor 
    public List<WapUser> findAllChatsOfUser() {
        WapUser user = userService.findUserById(UserUtil.getIdFromContext());
        return chatRepository.findAllOfUser(user)
                .stream()
                .map(chat -> userService.findUserById(chat.getParticipants()
                        .stream()
                        .filter(p -> p != user)
                        .map(WapUser::getId)
                        .findAny()
                        .orElse(UUID.randomUUID())))
                .collect(Collectors.toList());
    }

    public ChatDTO getOrCreateChat(UUID receiverId) throws UserNotFoundException, ChatException {
        WapUser sender = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        WapUser receiver = userService.findUserById(receiverId);
        if (sender == receiver) throw new ChatException("You cannot message youself");

        Chat chat = chatRepository
                .findDirectChat(sender, receiver)
                .orElseGet(() -> Chat.builder()
                        .type(ChatType.PERSONAL)
                        .participants(Set.of(sender, receiver))
                        .build());
        return chatRepository.save(chat).toDTO();
    }

    public Chat createGroup(GroupChatCreateRequest request, WapUser createdBy) {
        return Chat.builder()
                .type(ChatType.GROUP)
                .createdBy(createdBy)
                .participants(request.participants()
                        .stream()
                        .map(participantId -> {
                            try {
                                return userService.findUserById(participantId);
                            } catch (UserNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }).collect(Collectors.toSet()))
                .admins(Set.of(createdBy))
                .build();
    }

    public Chat addUserToGroup(WapUser issuer, UUID userId, UUID chatId) throws UserNotFoundException, ChatException {
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found"));
        if (!chat.getAdmins().contains(issuer)) {
            throw new ChatActionAccessException();
        }
        WapUser userToAdd = userService.findUserById(userId);
        if (chat.getParticipants().contains(userToAdd)) {
            throw new ChatException("That user is already in this group");
        }
        chat.getAdmins().add(userToAdd);
        return chatRepository.save(chat);
    }

    public Chat removeUserFromGroup(WapUser issuer, UUID userId, UUID chatId) throws UserNotFoundException, ChatException {
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found"));
        if (!chat.getAdmins().contains(issuer)) {
            throw new ChatActionAccessException();
        }
        WapUser userToRemove = userService.findUserById(userId);
        if (!chat.getParticipants().contains(userToRemove)) {
            throw new ChatException("That user is not in that group");
        }
        chat.getAdmins().remove(userToRemove);
        return chatRepository.save(chat);
    }

    public Chat renameGroup(UUID chatId, String groupName, WapUser issuer) throws ChatException {
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found"));
        if (!chat.getParticipants().contains(issuer)) {
            throw new ChatActionAccessException();
        }
        chat.setName(groupName);
        return chatRepository.save(chat);
    }

    public void deleteChat(UUID chatId, WapUser issuer) throws ChatException {
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new ChatException("Chat not found"));
        if (!chat.getAdmins().contains(issuer)) {
            throw new ChatActionAccessException();
        }
        chatRepository.deleteById(chatId);
    }
}
