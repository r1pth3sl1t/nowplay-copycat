package ua.asf.telegramspotifybot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.asf.telegramspotifybot.core.SpotifyBot;
import ua.asf.telegramspotifybot.core.entity.SpotifyUser;
import ua.asf.telegramspotifybot.core.entity.Token;
import ua.asf.telegramspotifybot.core.service.SpotifyAuthorizationService;
import ua.asf.telegramspotifybot.requests.spotify.SpotifyApiClient;
import ua.asf.telegramspotifybot.requests.telegram.TelegramApiClient;

@Controller
public class WebhookController {

    @Autowired
    private final SpotifyBot spotifyBot;

    @Autowired
    private SpotifyAuthorizationService service;

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private TelegramApiClient telegramApiClient;

    public WebhookController(SpotifyBot spotifyBot){
        this.spotifyBot = spotifyBot;
    }

    @PostMapping("/")
    @ResponseBody
    public PartialBotApiMethod<?> ping(@RequestBody Update update) {
        return spotifyBot.onWebhookUpdateReceived(update);
    }

    @GetMapping("/auth")
    public String auth(@RequestParam(value = "code", required = false) String code, @RequestParam(required = false) String state, @RequestParam(required = false) String error) {
        if(error != null) return "/templates/error.html";
        if(state == null || !service.isAllowedId(state)) return "/templates/error.html";
        Token token =  spotifyApiClient.getToken(code);
        service.saveCode(state, code, token);
        SpotifyUser user = service.findByUUID(state);
        telegramApiClient.sendMessage(user.getChatId(), "Login successfully. Welcome, " + spotifyApiClient.getSpotifyUsername(token));
        return "/templates/success.html";
    }

}
