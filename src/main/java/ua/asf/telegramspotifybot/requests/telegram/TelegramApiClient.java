package ua.asf.telegramspotifybot.requests.telegram;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ua.asf.telegramspotifybot.configuration.TelegramBotMetadata;
import ua.asf.telegramspotifybot.requests.spotify.entity.Track;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

@Component
public class TelegramApiClient {

    private final TelegramBotMetadata telegramBotMetadata;

    @Autowired
    public TelegramApiClient(TelegramBotMetadata telegramBotMetadata) {
        this.telegramBotMetadata = telegramBotMetadata;
    }

    @PostConstruct
    public void registerWebhook() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(null, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(MessageFormat.format("{0}bot{1}/setWebhook?url={2}", telegramBotMetadata.getApiUrl(), telegramBotMetadata.getBotToken(), telegramBotMetadata.getBotPath()),
                HttpMethod.GET,
                requestEntity,
                String.class);
    }

    public void sendMessage(Long chatId, String message) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("text", message);
        map.add("chat_id", chatId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                MessageFormat.format("{0}bot{1}/sendMessage", telegramBotMetadata.getApiUrl(), telegramBotMetadata.getBotToken()),
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }


    public void sendAudio(Long chatId, Track track) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("audio", this.getHttpEntityForBinaryFile(track.getId() + ".mp3",
                this.getRawBytesFromUrlFile(track.getPreviewUrl()),
                "audio"));

        map.add("chat_id", chatId);
        map.add("performer", track.getArtistsAsString());
        map.add("title", track.getName());
        map.add("thumbnail", this.getHttpEntityForBinaryFile("thumbnail.jpeg",
                this.getRawBytesFromUrlFile(track.getAlbum().getImages().get(2).getUrl()),
                "thumbnail"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(
                    MessageFormat.format("{0}bot{1}/sendAudio", telegramBotMetadata.getApiUrl(), telegramBotMetadata.getBotToken()),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getRawBytesFromUrlFile(String url) {
        URL myUrl;
        byte[] buf = new byte[4096];
        try {
            myUrl = new URL(url);
        } catch (MalformedURLException e) {
            return buf;
        }
        InputStream myUrlStream;
        try {
            myUrlStream = myUrl.openStream();
        } catch (IOException e) {
            return buf;
        }
        BufferedInputStream inputStream = new BufferedInputStream(myUrlStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int i;

        try {
            while ((i = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, i);
            }
        } catch (IOException e) {
            return buf;
        }
        return outputStream.toByteArray();
    }

    private HttpEntity<byte[]> getHttpEntityForBinaryFile(String filename, byte[] data, String fileType) {
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();

        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name(fileType)
                .filename(filename)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return new HttpEntity<>(data, fileMap);
    }
}
