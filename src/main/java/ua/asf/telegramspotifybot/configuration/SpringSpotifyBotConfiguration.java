package ua.asf.telegramspotifybot.configuration;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ua.asf.telegramspotifybot.configuration.metadata.TelegramBotMetadata;
import ua.asf.telegramspotifybot.core.SpotifyBot;

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

    @Bean
    public Reflections reflections() {
        return new Reflections("ua.asf.telegramspotifybot");
    }

}
