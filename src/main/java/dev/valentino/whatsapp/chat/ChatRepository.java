package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.user.WapUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("SELECT c FROM Chat c WHERE c.id = ?1 AND c.type = ?2")
    Optional<Chat> findById(UUID id, ChatType type);

    @Query("SELECT c FROM Chat c WHERE c.type = DIRECT AND ?1 MEMBER OF c.participants AND ?2 MEMBER OF c.participants")
    Optional<Chat> findDirectChatByParticipants(WapUser user1, WapUser user2);

    @Query("SELECT c FROM Chat c WHERE c.type = DIRECT AND ?1 MEMBER OF c.participants")
    List<Chat> findAllDirectChatsByUser(WapUser user);
}
