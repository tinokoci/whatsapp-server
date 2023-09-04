package dev.valentino.whatsapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<WapUser, UUID> {

    @Query("SELECT u FROM WapUser u WHERE u.username = ?1")
    Optional<WapUser> findByUsername(String username);

    @Query("SELECT u FROM WapUser u WHERE u.username LIKE %?1% AND u != ?2")
    List<WapUser> searchUsersByUsername(String username, WapUser excludedUser);
}

