package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.chat.exception.ChatActionAccessException;
import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.chat.request.GroupChatCreateRequest;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<Chat> findAllChatsByUserId(UUID userId) {
        return chatRepository.findAllByUserId(userId);
    }

    public Chat createChat(WapUser user1, UUID userUuid2) throws UserNotFoundException {
        Optional<Chat> optionalChat = chatRepository.findNormalChat(user1.getUuid(), userUuid2);

        if (optionalChat.isPresent()) {
            return optionalChat.get();
        }
        WapUser user2 = userService.findUserById(userUuid2);

        return Chat.builder()
                .isGroup(false)
                .createdBy(user1)
                .participants(Set.of(user1, user2))
                .admins(Set.of(user1))
                .build();
    }

    public Chat createGroup(GroupChatCreateRequest request, WapUser createdBy) {
        return Chat.builder()
                .isGroup(true)
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
