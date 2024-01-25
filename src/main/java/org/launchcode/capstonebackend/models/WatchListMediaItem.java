package org.launchcode.capstonebackend.models;

import java.util.Objects;

public class WatchListMediaItem extends AbstractEntity {

    private int tmdbId;
    private String mediaType;

    public WatchListMediaItem(int tmdbId, String mediaType) {
        this.tmdbId = tmdbId;
        this.mediaType = mediaType;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WatchListMediaItem that = (WatchListMediaItem) o;
        return tmdbId == that.tmdbId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tmdbId);
    }
}
