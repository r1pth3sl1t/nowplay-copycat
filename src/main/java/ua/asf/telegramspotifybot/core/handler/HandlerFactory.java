package ua.asf.telegramspotifybot.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HandlerFactory {

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private InlineQueryHandler inlineQueryHandler;

    @Autowired
    private KeyboardHandler keyboardHandler;


    public Handler getHandler(Update update) {
        if(update.hasInlineQuery()) return inlineQueryHandler;
        else if(update.hasCallbackQuery()) return keyboardHandler;
        else return messageHandler;
    }

}

