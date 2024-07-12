package ua.asf.telegramspotifybot.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ua.asf.telegramspotifybot.configuration.metadata.TelegramBotMetadata;
import ua.asf.telegramspotifybot.core.handler.HandlerFactory;


public class SpotifyBot extends SpringWebhookBot {

    private final TelegramBotMetadata telegramBotMetadata;

    @Autowired
    private HandlerFactory handlerFactory;

    @Autowired
    public SpotifyBot(SetWebhook setWebhook, TelegramBotMetadata telegramBotMetadata) {
        super(setWebhook, telegramBotMetadata.getBotToken());
        this.telegramBotMetadata = telegramBotMetadata;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return handlerFactory.getHandler(update).handle(update);
    }

    @Override
    public String getBotPath() {
        return telegramBotMetadata.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return telegramBotMetadata.getBotName();
    }

}
