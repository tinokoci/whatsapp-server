package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.Chat;
import dev.valentino.whatsapp.user.WapUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(nullable = false)
    private WapUser sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Chat chat;
}
