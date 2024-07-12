package ua.asf.telegramspotifybot.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ua.asf.telegramspotifybot.core.handler.ResponseCreator;

@Request(request = "/logout")
public class LogoutCommand extends Command {

    public LogoutCommand(Long chatId, ResponseCreator responseCreator) {
        super(chatId, responseCreator);
    }

    @Override
    public BotApiMethod<?> execute() {
        return responseCreator.logout(chatId);
    }
}
