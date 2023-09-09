package dev.valentino.whatsapp.chat.impl.direct;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.chat.ChatActionAccessException;
import dev.valentino.whatsapp.chat.ChatException;
import dev.valentino.whatsapp.chat.ChatRepository;
import dev.valentino.whatsapp.chat.ChatType;
import dev.valentino.whatsapp.message.Message;
import dev.valentino.whatsapp.message.MessageService;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectChatService {

    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final UserService userService;

    @Transactional
    public DirectChatDTO getOrCreateDirectChat(UUID recipientId) throws UserNotFoundException, ChatException {
        WapUser sender = UserUtil.getUserFromContext();
        WapUser recipient = userService.getUserById(recipientId);
        if (sender == recipient) throw new ChatException("You cannot message yourself");

        Chat chat = chatRepository
                .findDirectChatByParticipants(sender, recipient)
                .orElseGet(() -> {
                    Chat newChat = Chat.builder()
                            .type(ChatType.DIRECT)
                            .participants(Set.of(sender, recipient))
                            .build();
                    return chatRepository.save(newChat);
                });
        return getDirectChatDTO(chat, recipient);
    }

    // ugly exception handling, maybe refactor in the future
    @Transactional
    public List<DirectChatDTO> getUserDirectChats() {
        return chatRepository.findAllChatsByUser(UserUtil.getUserFromContext(), ChatType.DIRECT)
                .stream()
                .map(chat -> {
                    MessageDTO latestMessage;
                    try {
                        latestMessage = messageService.getLatestMessage(chat);
                    } catch (ChatActionAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (latestMessage == null) return null;
                    try {
                        WapUser recipient = getDirectChatRecipient(chat);
                        return getDirectChatDTO(chat, recipient, latestMessage);
                    } catch (ChatException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    // ugly exception handling, maybe refactor in the future
    @Transactional
    public List<DirectChatDTO> searchDirectChatsByUsername(String username) {
        return userService.getUsersByUsername(username)
                .stream()
                .map(user -> {
                    Chat chat = null;
                    MessageDTO latestMessage = null;
                    try {
                        chat = getDirectChatByRecipient(user);
                    } catch (ChatException ignored) {
                    }
                    if (chat != null) {
                        try {
                            latestMessage = messageService.getLatestMessage(chat);
                        } catch (ChatActionAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return getDirectChatDTO(chat, user, latestMessage);
                })
                .collect(Collectors.toList());
    }

    private WapUser getDirectChatRecipient(Chat chat) throws ChatException {
        WapUser sender = UserUtil.getUserFromContext();
        return chat.getParticipants()
                .stream()
                .filter(participant -> !participant.isSameAs(sender))
                .findFirst()
                .orElseThrow(() -> new ChatException("Missing recipient in direct chat " + chat.getId()));
    }

    private Chat getDirectChatByRecipient(WapUser recipient) throws ChatException {
        WapUser sender = UserUtil.getUserFromContext();
        return chatRepository
                .findDirectChatByParticipants(sender, recipient)
                .orElseThrow(() -> new ChatException("Chat not found"));
    }

    private DirectChatDTO getDirectChatDTO(Chat chat, WapUser recipient) {
        return getDirectChatDTO(chat, recipient, null);
    }

    private DirectChatDTO getDirectChatDTO(@Nullable Chat chat, WapUser recipient, MessageDTO latestMessage) {
        UUID chatId = chat == null ? null : chat.getId();
        return new DirectChatDTO(
                chatId,
                recipient.getId(),
                recipient.getUsername(),
                recipient.getAvatar(),
                latestMessage
        );
    }

}
