package ua.asf.telegramspotifybot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ua.asf.telegramspotifybot.core.handler.ResponseCreator;

@Component
public abstract class Command {

    protected Long chatId;

    protected ResponseCreator responseCreator;

    @Autowired
    public Command(Long chatId, ResponseCreator responseCreator) {
        this.chatId = chatId;
        this.responseCreator = responseCreator;
    }
    public abstract BotApiMethod<?> execute();
}
