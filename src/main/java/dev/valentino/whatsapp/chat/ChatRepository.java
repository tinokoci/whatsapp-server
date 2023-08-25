package dev.valentino.whatsapp.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("SELECT c FROM Chat c WHERE c.isGroup = false AND ?1 MEMBER OF c.participants AND ?2 MEMBER OF c.participants")
    Optional<Chat> findNormalChat(UUID userUuid1, UUID userUuid2);

    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.uuid = ?1")
    List<Chat> findAllByUserId(UUID userId);
}
