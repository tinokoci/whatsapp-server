package dev.valentino.whatsapp.chat;

import dev.valentino.whatsapp.user.WapUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("SELECT c FROM Chat c WHERE c.type = PERSONAL AND ?1 MEMBER OF c.participants AND ?2 MEMBER OF c.participants")
    Optional<Chat> findDirectChat(WapUser user1, WapUser user2);

    @Query("SELECT c FROM Chat c WHERE ?1 MEMBER OF c.participants")
    Optional<Chat> findAllOfUser(WapUser user);

    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.username = ?1")
    List<Chat> findAllByUserId(String username);
}
