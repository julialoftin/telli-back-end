package org.launchcode.capstonebackend.models.dto;

import jakarta.validation.constraints.NotNull;

public class MediaItemDTO {

    @NotNull
    private int tmdbId;

    @NotNull
    private String mediaType;

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
