package ua.asf.telegramspotifybot.requests.spotify;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ua.asf.telegramspotifybot.core.entity.Token;

import java.nio.charset.StandardCharsets;

public class SpotifyHttpRequestUtils {

    public static HttpHeaders getDefaultHeaderWithAuthorization(Token token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("Bearer " + token.getAccessToken());
        return headers;
    }

    public static String getBasicAuthHeader(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII) );
        return new String(encodedAuth);
    }

    public static ResponseEntity<String> getTemplateForUrl(String url, HttpEntity<?> requestEntity) {
        return new RestTemplate().exchange(url,
                HttpMethod.GET,
                requestEntity,
                String.class);
    }
}
