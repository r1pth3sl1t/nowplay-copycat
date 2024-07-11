package ua.asf.telegramspotifybot.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ua.asf.telegramspotifybot.core.handler.ResponseCreator;

public class NullCommand extends Command {

    public NullCommand(Long chatId, ResponseCreator responseCreator) {
        super(chatId, responseCreator);
    }

    @Override
    public BotApiMethod<?> execute() {
        return null;
    }
}
