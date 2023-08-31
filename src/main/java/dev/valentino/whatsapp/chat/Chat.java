package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.chat.dto.ChatDTO;
import dev.valentino.whatsapp.message.Message;
import dev.valentino.whatsapp.user.WapUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType type;

    private String name;
    private String image;

    @ManyToOne
    private WapUser createdBy;

    @ManyToMany
    @Builder.Default
    private Set<WapUser> participants = new HashSet<>();

    @ManyToMany
    @Builder.Default
    private Set<WapUser> admins = new HashSet<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    public ChatDTO toDTO() {
        return new ChatDTO(id.toString(), name, image);
    }
}
