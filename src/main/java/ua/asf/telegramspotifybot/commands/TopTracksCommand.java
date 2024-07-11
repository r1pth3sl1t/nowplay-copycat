package ua.asf.telegramspotifybot.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ua.asf.telegramspotifybot.core.handler.ResponseCreator;

@Request(request = "/top")
public class TopTracksCommand extends Command {

    public TopTracksCommand(Long chatId, ResponseCreator responseCreator) {
        super(chatId, responseCreator);
    }

    @Override
    public BotApiMethod<?> execute() {
        return responseCreator.topTracks(chatId);
    }
}
