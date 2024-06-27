package ua.asf.telegramspotifybot.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.asf.telegramspotifybot.core.entity.Token;

@Repository
public interface SpotifyTokensDao extends JpaRepository<Token, Integer> {
}
