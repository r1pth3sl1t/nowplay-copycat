package ua.asf.telegramspotifybot.requests.spotify.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties
public class Track {

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    private Album album;

    private String previewUrl;

    private List<Artist> artists;

    private int popularity;

    private String raw;


    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("artists")
    public List<Artist> getArtists() {
        return artists;
    }
    @JsonProperty("artists")
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    @JsonProperty("popularity")
    public int getPopularity() {
        return popularity;
    }

    @JsonProperty("popularity")
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return this.getArtistsAsString() + " - " + name;
    }

    public String getArtistsAsString() {
        if(artists == null) return "";
        StringBuilder artistsString = new StringBuilder();
        this.artists.forEach(t -> artistsString.append(t == null ? "" : t.getName() + ","));
        artistsString.deleteCharAt(artistsString.length() - 1);
        return artistsString.toString();
    }


    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @JsonProperty("preview_url")
    public String getPreviewUrl() {
        return previewUrl;
    }

    @JsonProperty("preview_url")
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }
}
