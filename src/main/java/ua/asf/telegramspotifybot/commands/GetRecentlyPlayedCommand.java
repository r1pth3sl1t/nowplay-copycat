package ua.asf.telegramspotifybot.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ua.asf.telegramspotifybot.core.handler.ResponseCreator;

@Request(request = "/recent")
public class GetRecentlyPlayedCommand extends Command{


    public GetRecentlyPlayedCommand(Long chatId, ResponseCreator responseCreator) {
        super(chatId, responseCreator);
    }

    @Override
    public BotApiMethod<?> execute() {
        return responseCreator.recentlyPlayedTracks(chatId);
    }
}
