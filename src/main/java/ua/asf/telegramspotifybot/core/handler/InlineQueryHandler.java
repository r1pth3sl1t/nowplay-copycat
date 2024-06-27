package ua.asf.telegramspotifybot.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultAudio;
import ua.asf.telegramspotifybot.core.service.SpotifyAuthorizationService;
import ua.asf.telegramspotifybot.requests.spotify.SpotifyApiClient;
import ua.asf.telegramspotifybot.requests.spotify.entity.Track;

import java.util.LinkedList;
import java.util.List;

@Component
public class InlineQueryHandler implements Handler {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private SpotifyAuthorizationService service;

    @Override
    public BotApiMethod<?> handle(Update update) {
        if(!service.isAuthorized(update.getInlineQuery().getFrom().getId())) return null;

        service.refreshToken(update.getInlineQuery().getFrom().getId());

        List<Track> recentlyPlayedTracks = this.spotifyApiClient.getRecentlyPlayedTracks(
                this.service.getTokenByChatId(update.getInlineQuery().getFrom().getId())
        );
        List<InlineQueryResultAudio> result = new LinkedList<>();
        int i = 0;
        for (Track t : recentlyPlayedTracks) {
            result.add(InlineQueryResultAudio.builder()
                    .id(String.valueOf(i++))
                    .title(t.getName())
                    .performer(t.getArtistsAsString())
                    .audioUrl(t.getPreviewUrl())
                    .build());
        }
        return AnswerInlineQuery.builder()
                .inlineQueryId(update.getInlineQuery().getId())
                .results(result)
                .build();
    }
}
