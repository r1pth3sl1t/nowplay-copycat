package ua.asf.telegramspotifybot.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ua.asf.telegramspotifybot.core.handler.ResponseCreator;

@Request(request = "/now")
public class NowPlayingCommand extends Command {

    public NowPlayingCommand(Long chatId, ResponseCreator responseCreator) {
        super(chatId, responseCreator);
    }

    @Override
    public BotApiMethod<?> execute() {
        return responseCreator.nowPlaying(chatId);
    }
}
