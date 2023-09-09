package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.chat.ChatActionAccessException;
import dev.valentino.whatsapp.chat.ChatException;
import dev.valentino.whatsapp.chat.ChatRepository;
import dev.valentino.whatsapp.chat.ChatService;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.message.exception.MessageException;
import dev.valentino.whatsapp.message.request.MessageSendRequest;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final MessageRepository messageRepository;

    @Transactional
    public MessageDTO sendMessage(MessageSendRequest request) throws UserNotFoundException, ChatException {
        WapUser sender = UserUtil.getUserFromContext();
        Chat chat = chatRepository
                .findById(request.chatId())
                .orElseThrow(() -> new ChatException("Chat not found"));
        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .text(request.text())
                .build();
        chat.getMessages().add(message);
        chatRepository.save(chat);
        return message.toDTO();
    }

    public Message findMessageById(UUID messageId) throws MessageException {
        return messageRepository
                .findById(messageId)
                .orElseThrow(() -> new MessageException("Message not found"));
    }

    public void deleteMessage(UUID messageId) throws MessageException, ChatActionAccessException {
        WapUser user = UserUtil.getUserFromContext();
        Message message = findMessageById(messageId);

        if (!message.getSender().getId().equals(user.getId())) {
            throw new ChatActionAccessException();
        }
        messageRepository.deleteById(messageId);
    }

    public List<MessageDTO> getAllMessagesInChat(Chat chat) throws ChatActionAccessException {
        WapUser user = UserUtil.getUserFromContext();
        if (!chat.isParticipant(user)) {
            throw new ChatActionAccessException();
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return messageRepository.findAllInChat(chat, sort)
                .stream()
                .map(Message::toDTO)
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getAllMessagesInChat(UUID chatId) throws ChatException {
        Chat chat = chatService.getChatById(chatId);
        return getAllMessagesInChat(chat);
    }

    public MessageDTO getLatestMessage(Chat chat) throws ChatActionAccessException {
        List<MessageDTO> messages = getAllMessagesInChat(chat);
        return messages.isEmpty() ? null : messages.get(0);
    }
}
