package dev.valentino.whatsapp.chat.impl.direct;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.chat.ChatActionAccessException;
import dev.valentino.whatsapp.chat.ChatException;
import dev.valentino.whatsapp.chat.ChatRepository;
import dev.valentino.whatsapp.chat.ChatType;
import dev.valentino.whatsapp.message.MessageRepository;
import dev.valentino.whatsapp.message.MessageService;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

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

    public Chat getDirectChatById(UUID chatId) throws ChatException {
        return chatRepository
                .findById(chatId, ChatType.DIRECT)
                .orElseThrow(() -> new ChatException("Chat not found"));
    }

    public Chat getDirectChatByRecipient(WapUser recipient) throws ChatException {
        WapUser sender = UserUtil.getUserFromContext();
        return chatRepository
                .findDirectChatByParticipants(sender, recipient)
                .orElseThrow(() -> new ChatException("Chat not found"));
    }

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

    public List<DirectChatDTO> getUserDirectChats() {
        return chatRepository.findAllDirectChatsByUser(UserUtil.getUserFromContext())
                .stream()
                .map(chat -> {
                    String latestMessageText = messageService.getLatestMessageText(chat);
                    if (latestMessageText.isEmpty()) return null;
                    try {
                        WapUser recipient = getDirectChatRecipient(chat);
                        return getDirectChatDTO(chat, recipient, latestMessageText);
                    } catch (ChatException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<DirectChatDTO> searchDirectChatsByUsername(String username) {
        return userService.searchUsersByUsername(username)
                .stream()
                .map(user -> {
                    Chat chat = null;
                    String latestMessageText = null;
                    try {
                        chat = getDirectChatByRecipient(user);
                    } catch (ChatException ignored) {
                    }
                    if (chat != null) {
                        latestMessageText = messageService.getLatestMessageText(chat);
                    }
                    return getDirectChatDTO(chat, user, latestMessageText);
                })
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getDirectChatMessages(UUID chatId) throws ChatException {
        WapUser user = UserUtil.getUserFromContext();
        Chat chat = getDirectChatById(chatId);

        if (!chat.isParticipant(user)) {
            throw new ChatActionAccessException();
        }
        return messageService.getAllMessages(chat);
    }

    private WapUser getDirectChatRecipient(Chat chat) throws ChatException {
        WapUser sender = UserUtil.getUserFromContext();
        return chat.getParticipants()
                .stream()
                .filter(participant -> !participant.isSameAs(sender))
                .findFirst()
                .orElseThrow(() -> new ChatException("Missing recipient in direct chat " + chat.getId()));
    }

    private DirectChatDTO getDirectChatDTO(Chat chat, WapUser recipient) {
        return getDirectChatDTO(chat, recipient, null);
    }

    private DirectChatDTO getDirectChatDTO(@Nullable Chat chat, WapUser recipient, String latestMessageText) {
        UUID chatId = chat == null ? null : chat.getId();
        return new DirectChatDTO(
                chatId,
                recipient.getId(),
                recipient.getUsername(),
                recipient.getAvatar(),
                latestMessageText
        );
    }
}
