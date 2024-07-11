package ua.asf.telegramspotifybot.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.asf.telegramspotifybot.core.service.SpotifyAuthorizationService;
import ua.asf.telegramspotifybot.requests.spotify.SpotifyApiClient;
import ua.asf.telegramspotifybot.requests.spotify.entity.Track;
import ua.asf.telegramspotifybot.requests.telegram.TelegramApiClient;

@Component
public class ResponseCreator {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private TelegramApiClient telegramApiClient;

    @Autowired
    private SpotifyAuthorizationService spotifyAuthorizationService;

    public SendMessage recentlyPlayedTracks(Long chatId) {
        StringBuilder list = new StringBuilder();

        int idx = 1;
        for(var i : spotifyApiClient.getRecentlyPlayedTracks(spotifyAuthorizationService.getTokenByChatId(chatId))) {
            list.append(idx++).append(". ").append(i).append('\n');
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(list.toString())
                .build();
    }

    public SendMessage nowPlaying(Long chatId) {

        Track track = spotifyApiClient.getCurrentlyPlayingSong(spotifyAuthorizationService.getTokenByChatId(chatId));

        if(track != null && track.getPreviewUrl() != null) {
            this.telegramApiClient.sendAudio(chatId, track);
            return null;
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text("Cannot process your '/now' request.")
                .build();
    }

    public SendMessage topTracks(Long chatId) {
        StringBuilder list = new StringBuilder();

        int idx = 1;
        for(var i : spotifyApiClient.getTopTracks(spotifyAuthorizationService.getTokenByChatId(chatId))) {
            list.append(idx++).append(". ").append(i).append('\n');
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(list.toString())
                .build();
    }

    public SendMessage logout(Long chatId) {
        boolean success = spotifyAuthorizationService.logout(chatId);
        String message = success ? "Log out successfully." : "Log in account not found.";
        return SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
    }


}
