package ua.asf.telegramspotifybot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpotifyApiMetaData {

    @Value("${spotify-api.client-id}")
    private String clientId;

    @Value("${spotify-api.client-secret}")
    private String clientSecret;

    @Value("${spotify-api.redirect-uri}")
    private String redirectUri;

    @Value("${spotify-api.auth-url}")
    private String authUrl;

    @Value("${spotify-api.api-url}")
    private String apiUrl;

    @Value("${spotify-api.scopes-list}")
    private String scopesList;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getAuthUrl() {
        return authUrl;
    }


    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScopesList() {
        return scopesList;
    }
}
