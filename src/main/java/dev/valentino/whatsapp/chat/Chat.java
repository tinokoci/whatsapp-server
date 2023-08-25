package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.message.Message;
import dev.valentino.whatsapp.user.WapUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    private boolean isGroup;

    @ManyToOne
    private WapUser createdBy;

    @ManyToMany
    @Builder.Default
    private final Set<WapUser> participants = new HashSet<>();

    @ManyToMany
    @Builder.Default
    private final Set<WapUser> admins = new HashSet<>();

    @OneToMany(mappedBy = "chat")
    @Builder.Default
    private final List<Message> messages = new ArrayList<>();
}
