package ua.asf.telegramspotifybot.requests.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import ua.asf.telegramspotifybot.configuration.metadata.SpotifyApiMetaData;
import ua.asf.telegramspotifybot.core.entity.Token;
import ua.asf.telegramspotifybot.requests.spotify.entity.Track;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import static ua.asf.telegramspotifybot.requests.spotify.SpotifyHttpRequestUtils.*;

@Component
public class SpotifyApiClient {

    @Autowired
    private SpotifyApiMetaData spotifyApiMetaData;

    @Autowired
    private RestTemplate restTemplate;

    public Token getToken(String authCode) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        map.add("grant_type", "authorization_code");
        map.add("code", authCode);
        map.add("redirect_uri", spotifyApiMetaData.getRedirectUri() + "/auth");
        String auth = spotifyApiMetaData.getClientId() + ":" + spotifyApiMetaData.getClientSecret();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII) );
        String authHeader = new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(authHeader);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<Token> response = restTemplate.postForEntity(
                MessageFormat.format("{0}token", spotifyApiMetaData.getAuthUrl()),
                requestEntity,
                Token.class
        );
        return response.getBody();
    }

    public Token refreshToken(Token token) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", token.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(getBasicAuthHeader(this.spotifyApiMetaData.getClientId(), this.spotifyApiMetaData.getClientSecret()));

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<Token> response = restTemplate.postForEntity(
                MessageFormat.format("{0}token", spotifyApiMetaData.getAuthUrl()),
                requestEntity,
                Token.class
        );
        if(response.getBody() == null) return token;

        token.setAccessToken(response.getBody().getAccessToken());

       return token;
    }

    public String getSpotifyUsername(Token token) {

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(null,
                getDefaultHeaderWithAuthorization(token));

        ResponseEntity<String> response = restTemplate.exchange(
                MessageFormat
                        .format("{0}v1/me", spotifyApiMetaData.getApiUrl()),
                HttpMethod.GET,
                requestEntity,
                String.class);
        try {
           return new ObjectMapper().readTree(response.getBody()).get("display_name").asText();
        } catch (JsonProcessingException e) {
            return "";
        }

    }

    public Track getCurrentlyPlayingSong(Token token) {

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(null,
                getDefaultHeaderWithAuthorization(token));

        ResponseEntity<String> response = restTemplate.exchange(
                MessageFormat
                        .format("{0}v1/me/player/currently-playing", spotifyApiMetaData.getApiUrl()),
                HttpMethod.GET,
                requestEntity,
                String.class);

        if(response.getBody() == null) return null;
        try {
            String trackString = new ObjectMapper().readTree(response.getBody()).get("item").toString();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Track track = objectMapper.readValue(trackString, Track.class);
            if(track.getId() == null || track.getId().isEmpty()) return null;
            return track;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public List<Track> getTopTracks(Token token) {

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(null,
                getDefaultHeaderWithAuthorization(token));

        ResponseEntity<String> response = restTemplate.exchange(
                MessageFormat
                        .format("{0}v1/me/top/tracks?limit=10&time_range=short_term", spotifyApiMetaData.getApiUrl()),
                HttpMethod.GET,
                requestEntity,
                String.class);

        try {
            String arr = new ObjectMapper().readTree(response.getBody()).get("items").toString();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(arr, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Track> getRecentlyPlayedTracks(Token token) {

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(null,
                getDefaultHeaderWithAuthorization(token));

        ResponseEntity<String> response = restTemplate.exchange(
                MessageFormat
                        .format("{0}v1/me/player/recently-played?limit=5", spotifyApiMetaData.getApiUrl()),
                HttpMethod.GET,
                requestEntity,
                String.class);

        try {
            String arr = new ObjectMapper().readTree(response.getBody()).get("items").toString();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<TrackWrapper> tracks = objectMapper.readValue(arr, new TypeReference<>() {
            });

            List<Track> list = new LinkedList<>();
            tracks.forEach(t -> list.add(t.track));
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Track> findTrack(Token token, String query) {


        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(null,
                getDefaultHeaderWithAuthorization(token));

        ResponseEntity<String> response = restTemplate.exchange(
                MessageFormat
                        .format("{0}v1/search?q=track:{1}&type=track", spotifyApiMetaData.getApiUrl(), query),
                HttpMethod.GET,
                requestEntity,
                String.class);

        try {
            String arr = new ObjectMapper().readTree(response.getBody()).get("tracks").get("items").toString();
            System.out.println(arr);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(arr, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private static class TrackWrapper {
        @JsonProperty("track")
        Track track;
    }

}
