package ua.asf.telegramspotifybot.core.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "spotify_subscribers")
public class SpotifyUser {

    @Column(name = "uuid")
    @Id
    private String uuid;

    @Column(name = "chat_id")
    private Long chatId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "auth_code")
    private String authCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id")
    private Token token = new Token();

    public SpotifyUser() {

    }

    public SpotifyUser(Long chatId, String authCode) {
        this.chatId = chatId;
        this.authCode = authCode;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
