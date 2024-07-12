package ua.asf.telegramspotifybot.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.asf.telegramspotifybot.commands.Dispatcher;
import ua.asf.telegramspotifybot.configuration.SpotifyApiMetaData;
import ua.asf.telegramspotifybot.core.handler.keyboard.SpotifyInlineKeyboardButtonsFactory;
import ua.asf.telegramspotifybot.core.service.SpotifyAuthorizationService;
import ua.asf.telegramspotifybot.requests.spotify.SpotifyApiClient;
import ua.asf.telegramspotifybot.requests.spotify.entity.Track;
import ua.asf.telegramspotifybot.requests.telegram.TelegramApiClient;


@Component
public class MessageHandler implements Handler {

    @Autowired
    private SpotifyAuthorizationService service;

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private SpotifyApiMetaData spotifyApiMetaData;

    @Autowired
    private TelegramApiClient telegramApiClient;

    @Autowired
    private Dispatcher dispatcher;

    @Autowired
    private Executor executor;

    @Override
    public BotApiMethod<?> handle(Update update) {
        if(!update.hasMessage()) return null;
        if(!update.getMessage().hasText()) return null;
        Long chatId = update.getMessage().getFrom().getId();
        String text = update.getMessage().getText();

        if(!service.isAuthorized(chatId)) {
            service.addNewAllowedId(chatId);
            return this.sendMessageTemplate(chatId,
                    "It looks like you haven't authorized yet. Go by this link to authorize")
                    .replyMarkup(SpotifyInlineKeyboardButtonsFactory.
                            getAuthorizationInlineKeyboardMarkup(this.spotifyApiMetaData, service.getUUIDbyChatId(chatId)))
                    .build();
        }
        else {
            service.refreshToken(chatId);
            executor.setCommand(dispatcher.commandFromRequest(text, chatId));
            return executor.executeCommand();
        }
    }


    private SendMessage.SendMessageBuilder sendMessageTemplate(Long chatId, String message) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(message);
    }

}
