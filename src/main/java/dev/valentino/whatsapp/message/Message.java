package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.message.dto.MessageDTO;
import dev.valentino.whatsapp.user.WapUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
