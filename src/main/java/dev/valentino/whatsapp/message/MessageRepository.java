package dev.valentino.whatsapp.message;

import dev.valentino.whatsapp.chat.Chat;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m WHERE m.chat = ?1")
    List<Message> findAllInChat(Chat chat);


    @Query("SELECT m FROM Message m WHERE m.chat = ?1")
    List<Message> findAllInChat(Chat chat, Sort sort);

    @Query("SELECT m FROM Message m ORDER BY m.timestamp DESC LIMIT 1")
    Optional<Message> findAll(Chat chat);
}
