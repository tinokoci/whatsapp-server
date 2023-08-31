package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.chat.ChatRepository;
import dev.valentino.whatsapp.chat.ChatService;
import dev.valentino.whatsapp.chat.exception.ChatActionAccessException;
import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.message.exception.MessageException;
import dev.valentino.whatsapp.message.request.MessageSendRequest;
import dev.valentino.whatsapp.user.UserService;
import dev.valentino.whatsapp.user.WapUser;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import dev.valentino.whatsapp.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageDTO sendPersonalMessage(MessageSendRequest request) throws UserNotFoundException, ChatException {
        WapUser sender = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        WapUser receiver = userService.findUserById(request.entityId());
        Chat chat = chatService.findDirectChat(sender, receiver);
        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .text(request.text())
                .build();
        chat.getMessages().add(message);
        chatRepository.save(chat);
        return message.toDTO();
    }

    public Message sendGroupMessage(MessageSendRequest request) throws UserNotFoundException, ChatException {
        WapUser sender = userService.findUserByUsername(UserUtil.getUsernameFromContext());
        Chat chat = chatService.findChatById(request.entityId());

        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .text(request.text())
                .build();

        System.out.println("BEFORE SAVE");
        return messageRepository.save(message);
    }


    public List<MessageDTO> getDirectChatMessages(UUID chatId, WapUser sender) throws ChatException {
        WapUser receiver = userService.findUserById(chatId);
        Chat chat = chatService.findDirectChat(sender, receiver);
        if (!chat.getParticipants().contains(sender)) {
            throw new ChatActionAccessException();
        }
        return messageRepository.findAllByChatId(chat.getId())
                .stream()
                .map(Message::toDTO)
                .collect(Collectors.toList());
    }

    public Message findMessageById(UUID messageId) throws MessageException {
        return messageRepository
                .findById(messageId)
                .orElseThrow(() -> new MessageException("Message not found"));
    }

    public void deleteMessage(UUID messageId, WapUser user) throws MessageException, ChatActionAccessException {
        Message message = findMessageById(messageId);

        if (!message.getSender().getId().equals(user.getId())) {
            throw new ChatActionAccessException();
        }
        messageRepository.deleteById(messageId);
    }

}
