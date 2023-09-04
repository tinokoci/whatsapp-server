package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.message.Message;
import dev.valentino.whatsapp.user.WapUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private byte[] image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType type;

    @ManyToMany
    @Builder.Default
    private Set<WapUser> participants = new HashSet<>();

    @ManyToMany
    @Builder.Default
    private Set<WapUser> admins = new HashSet<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    public boolean isAdmin(WapUser user) {
        return admins
                .stream()
                .anyMatch(admin -> admin.isSameAs(user));
    }

    public boolean isParticipant(WapUser user) {
        return participants
                .stream()
                .anyMatch(participant -> participant.isSameAs(user));
    }
}
