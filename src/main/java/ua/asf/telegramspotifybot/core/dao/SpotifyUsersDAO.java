package ua.asf.telegramspotifybot.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.asf.telegramspotifybot.core.entity.SpotifyUser;

import java.util.Optional;

@Repository
public interface SpotifyUsersDAO extends JpaRepository<SpotifyUser, String> {

    Optional<SpotifyUser> findByChatId(Long s);
}
