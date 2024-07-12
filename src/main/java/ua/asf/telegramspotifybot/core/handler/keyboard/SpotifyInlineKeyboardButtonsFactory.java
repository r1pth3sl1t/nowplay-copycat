package ua.asf.telegramspotifybot.core.handler.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.asf.telegramspotifybot.configuration.metadata.SpotifyApiMetaData;

import java.text.MessageFormat;
import java.util.List;

public class SpotifyInlineKeyboardButtonsFactory {

    public static InlineKeyboardMarkup getAuthorizationInlineKeyboardMarkup(SpotifyApiMetaData spotifyApiMetaData, String uuid) {
        InlineKeyboardMarkup btn = new InlineKeyboardMarkup();
        btn.setKeyboard(List.of(List.of(
                InlineKeyboardButton.builder()
                    .text("Spotify")
                    .url(
                            MessageFormat.format("https://accounts.spotify.com/authorize?" +
                                    "response_type=code&client_id={0}&" +
                                    "redirect_uri={1}{2}&" +
                                    "state={3}&" + "scope={4}",
                                    spotifyApiMetaData.getClientId(),
                                    spotifyApiMetaData.getRedirectUri(),
                                    "/auth",
                                    uuid, spotifyApiMetaData.getScopesList()))
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Logout")
                    .callbackData("/logout/" + uuid)
                    .build()
        )));
        return btn;
    }
}
