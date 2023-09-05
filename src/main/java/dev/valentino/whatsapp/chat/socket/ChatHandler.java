package dev.valentino.whatsapp.chat.socket;

import dev.valentino.whatsapp.message.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatHandler {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/chat/public")
    public MessageDTO receiveMessage(@Payload MessageDTO message) {
        System.out.println("received: " + message.toString());
        simpMessagingTemplate.convertAndSend("/chat/" + message.chatId(), message);
        return message;
    }
}
