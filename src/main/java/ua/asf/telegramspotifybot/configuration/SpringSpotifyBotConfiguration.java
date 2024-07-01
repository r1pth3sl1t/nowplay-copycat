package ua.asf.telegramspotifybot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ua.asf.telegramspotifybot.core.SpotifyBot;

import java.text.MessageFormat;

@Configuration
public class SpringSpotifyBotConfiguration {

    @Bean
    public SetWebhook setWebhook(TelegramBotMetadata telegramBotMetadata) {
        return SetWebhook.builder().url(telegramBotMetadata.getBotPath()).build();
    }

    @Bean
    public SpotifyBot launchBot(SetWebhook setWebhook, TelegramBotMetadata telegramBotMetadata) {
        return new SpotifyBot(setWebhook, telegramBotMetadata);
    }

}