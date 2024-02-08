package org.launchcode.capstonebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

@Entity
public class MediaItem {

    @Id
    @NotNull
    private int tmdbId;

    @NotNull
    private String mediaType;

    @JsonIgnore
    @ManyToMany(mappedBy = "mediaItems", cascade = CascadeType.ALL)
    private List<WatchList> watchLists;

    public MediaItem() {}

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


