package dev.valentino.whatsapp.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.user.WapUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    @Builder.Default
    private Long timestamp = System.currentTimeMillis();

    @ManyToOne
    private WapUser sender;

    @ManyToOne
    private Chat chat;

    public MessageDTO toDTO() {
        return new MessageDTO(sender.getId().toString(), text, timestamp);
    }
}
