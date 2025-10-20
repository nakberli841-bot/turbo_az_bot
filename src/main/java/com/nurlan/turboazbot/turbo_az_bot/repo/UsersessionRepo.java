package com.nurlan.turboazbot.turbo_az_bot.repo;

import com.nurlan.turboazbot.turbo_az_bot.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersessionRepo extends JpaRepository<UserSession, Long> {

     UserSession findByEmail(String email);
    Optional<UserSession> findByTelegramChatId(Long chatId);


}
