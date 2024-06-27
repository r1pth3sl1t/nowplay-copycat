package ua.asf.telegramspotifybot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotMetadata {
    @Value("${telegram-bot.token}")
    private String botToken;

    @Value("${telegram-bot.name}")
    private String botName;

    @Value("${telegram.webhook-path}")
    private String botPath;

    @Value("${telegram.api-url}")
    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    public String getBotPath() {
        return botPath;
    }
}
