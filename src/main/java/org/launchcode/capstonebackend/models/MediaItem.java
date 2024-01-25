package org.launchcode.capstonebackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class MediaItem extends AbstractEntity {

    private int tmdbId;
    private String mediaType;

    @ManyToMany(mappedBy = "mediaItems")
    private List<WatchList> watchLists;

    public MediaItem(int tmdbId, String mediaType) {
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

    public List<WatchList> getWatchLists() {
        return watchLists;
    }

    public void setWatchLists(List<WatchList> watchLists) {
        this.watchLists = watchLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MediaItem that = (MediaItem) o;
        return tmdbId == that.tmdbId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tmdbId);
    }
}
