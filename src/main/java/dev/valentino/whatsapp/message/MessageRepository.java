package dev.valentino.whatsapp.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m JOIN m.chat c WHERE c.uuid = ?1")
    List<Message> findAllByChatId(UUID chatId);
}
