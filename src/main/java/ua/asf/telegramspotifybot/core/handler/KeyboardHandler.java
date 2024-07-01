package ua.asf.telegramspotifybot.core.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.asf.telegramspotifybot.core.service.SpotifyAuthorizationService;

@Component
public class KeyboardHandler implements Handler {

    @Autowired
    private SpotifyAuthorizationService service;

    @Override
    public BotApiMethod<?> handle(Update update) {
        if(!update.hasCallbackQuery()) return null;
        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        if(update.getCallbackQuery().getData().startsWith("/logout")) {
            boolean success = service.logout(chatId);
            String message = success ? "Log out successfully." : "Log in account not found.";
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(message)
                    .build();
        }


        return null;
    }
}
