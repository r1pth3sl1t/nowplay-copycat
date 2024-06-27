package ua.asf.telegramspotifybot.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.asf.telegramspotifybot.core.dao.SpotifyTokensDao;
import ua.asf.telegramspotifybot.core.dao.SpotifyUsersDAO;
import ua.asf.telegramspotifybot.core.entity.SpotifyUser;
import ua.asf.telegramspotifybot.core.entity.Token;
import ua.asf.telegramspotifybot.requests.spotify.SpotifyApiClient;
import ua.asf.telegramspotifybot.requests.telegram.TelegramApiClient;

import java.util.*;

@Service
public class SpotifyAuthorizationService {

    @Autowired
    private SpotifyUsersDAO spotifyUsersDAO;

    @Autowired
    private SpotifyTokensDao spotifyTokensDao;

    @Autowired
    private TelegramApiClient telegramApiClient;

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    public SpotifyAuthorizationService(SpotifyUsersDAO dao) {
        this.spotifyUsersDAO = dao;
    }

    public boolean isAuthorized(Long chatId) {
        Optional<SpotifyUser> user = spotifyUsersDAO.findByChatId(chatId);
        return user.isPresent() && user.get().getAuthCode() != null;
    }


    public boolean logout(Long chatId) {
        Optional<SpotifyUser> user = spotifyUsersDAO.findByChatId(chatId);
        if(user.isEmpty()) return false;
        this.spotifyUsersDAO.delete(user.get());
        return true;
    }

    public void addNewAllowedId(Long chatId) {
        if(spotifyUsersDAO.findByChatId(chatId).isEmpty()) {
            SpotifyUser user = new SpotifyUser();
            user.setUuid(UUID.randomUUID().toString());
            user.setChatId(chatId);
            this.spotifyUsersDAO.save(user);
        }
    }

    public String getUUIDbyChatId(Long chatId) {
        return spotifyUsersDAO.findByChatId(chatId).orElse(new SpotifyUser()).getUuid();
    }

    public boolean isAllowedId(String uuid) {
        return spotifyUsersDAO.existsById(uuid);
    }

    public void saveCode(String uuid, String code, Token token) {
        Optional<SpotifyUser> user = spotifyUsersDAO.findById(uuid);
        if(user.isEmpty()) return;
        user.get().setAuthCode(code);
        user.get().getToken().setAccessToken(token.getAccessToken());
        user.get().getToken().setRefreshToken(token.getRefreshToken());
        spotifyUsersDAO.save(user.get());
    }

    public SpotifyUser findByUUID(String uuid) {
        return spotifyUsersDAO.findById(uuid).orElse(null);
    }

    public void refreshToken(Long chatId) {

        Optional<SpotifyUser> user = spotifyUsersDAO.findByChatId(chatId);
        if(user.isEmpty()) return;

        user.get().setToken(spotifyApiClient.refreshToken(user.get().getToken()));
    }

    public Token getTokenByChatId(Long chatId) {
        Optional<SpotifyUser> user = spotifyUsersDAO.findByChatId(chatId);
        return user.map(SpotifyUser::getToken).orElse(null);

    }

    public int hash(Long value) {
        return value.hashCode();
    }


    public SpotifyUser findById(Long id) {
        return spotifyUsersDAO.findByChatId(id).orElse(null);
    }
}
