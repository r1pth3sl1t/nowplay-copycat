package ua.asf.telegramspotifybot.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultAudio;
import ua.asf.telegramspotifybot.core.service.SpotifyAuthorizationService;
import ua.asf.telegramspotifybot.requests.spotify.SpotifyApiClient;
import ua.asf.telegramspotifybot.requests.spotify.entity.Track;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Component
public class InlineQueryHandler implements Handler {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private SpotifyAuthorizationService service;

    @Override
    public BotApiMethod<?> handle(Update update) {

        if(!service.isAuthorized(update.getInlineQuery().getFrom().getId())) return AnswerInlineQuery.builder()
                .inlineQueryId(update.getInlineQuery().getId())
                .result(InlineQueryResultArticle.builder()
                        .id(UUID.randomUUID().toString())
                        .title("Authorized account not found")
                        .url("https://t.me/asf000000_bot")
                        .inputMessageContent(new InputTextMessageContent("."))
                        .build())
                .cacheTime(1)
                .isPersonal(true)
                .build();

        service.refreshToken(update.getInlineQuery().getFrom().getId());

        List<Track> playedTracks =  this.spotifyApiClient.getRecentlyPlayedTracks(
                this.service.getTokenByChatId(update.getInlineQuery().getFrom().getId())
        );
        List<InlineQueryResultAudio> result = new LinkedList<>();
        for (Track t : playedTracks) {
            result.add(InlineQueryResultAudio.builder()
                    .id(UUID.randomUUID().toString())
                    .title(t.getName())
                    .performer(t.getArtistsAsString())
                    .audioUrl(t.getPreviewUrl())
                    .build());
        }
        return AnswerInlineQuery.builder()
                .inlineQueryId(update.getInlineQuery().getId())
                .results(result)
                .cacheTime(1)
                .isPersonal(true)
                .build();
    }
}