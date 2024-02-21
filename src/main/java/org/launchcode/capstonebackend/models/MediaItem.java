package org.launchcode.capstonebackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class MediaItem {

    @Id
    @NotNull
    @Valid
    private int tmdbId;

    @NotNull
    private String mediaType;

    @JsonIgnore
    @ManyToMany(mappedBy = "mediaItems", cascade = CascadeType.ALL)
    private List<WatchList> watchLists;

    @JsonIgnore
    @ManyToMany
    private final List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "mediaItem")
    @JsonIgnore
    private List<Review> reviews;

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

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
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


