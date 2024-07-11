package ua.asf.telegramspotifybot.core.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface Handler {
    BotApiMethod<?> handle(Update update);
}
