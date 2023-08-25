package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.chat.exception.ChatActionAccessException;
import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.chat.ChatService;
import dev.valentino.whatsapp.message.exception.MessageException;
import dev.valentino.whatsapp.message.request.SendMessageRequest;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatService chatService;
    private final MessageRepository messageRepository;
    private final UserService userService;

    public Message sendMessage(SendMessageRequest request) throws UserNotFoundException, ChatException {
        WapUser sender = userService.findUserById(request.senderId());
        Chat chat = chatService.findChatById(request.chatId());
        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .content(request.content())
                .timestamp(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }

    public List<Message> getChatMessages(UUID chatId, WapUser user) throws ChatException {
        Chat chat = chatService.findChatById(chatId);
        if (!chat.getParticipants().contains(user)) {
            throw new ChatActionAccessException();
        }
        return messageRepository.findAllByChatId(chatId);
    }

    public Message findMessageById(UUID messageId) throws MessageException {
        return messageRepository
                .findById(messageId)
                .orElseThrow(() -> new MessageException("Message not found"));
    }

    public void deleteMessage(UUID messageId, WapUser user) throws MessageException, ChatActionAccessException {
        Message message = findMessageById(messageId);

        if (!message.getSender().getUuid().equals(user.getUuid())) {
            throw new ChatActionAccessException();
        }
        messageRepository.deleteById(messageId);
    }

}
