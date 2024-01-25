package org.launchcode.capstonebackend.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class WatchList extends AbstractEntity {

    private String name;
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "watchlist_mediaitem",
            joinColumns = @JoinColumn(name = "watchlist_id"),
            inverseJoinColumns = @JoinColumn(name = "mediaitem_id")
    )
    private List<MediaItem> mediaItems = new ArrayList<>();

    public WatchList(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public WatchList() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public void addMediaItemToList(MediaItem mediaItem) {
        mediaItems.add(mediaItem);
    }

    @Override
    public String toString() {
        return name;
    }


}
