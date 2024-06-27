package ua.asf.telegramspotifybot.requests.spotify.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties
public class Album {

    private List<Cover> images;

    public List<Cover> getImages() {
        return images;
    }

    public void setImages(List<Cover> images) {
        this.images = images;
    }
}
