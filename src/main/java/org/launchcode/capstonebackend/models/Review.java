package org.launchcode.capstonebackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Review extends AbstractEntity {

    @NotNull
    @Size(min = 5, max = 75, message = "Title must be between 5 and 75 characters.")
    private String title;

    @NotNull
    @Size(min = 50, max = 5000, message = "Review must be between 50 and 1000 characters.")
    @Column(length = 5000)
    private String reviewBody;

    @ManyToOne
    @JoinColumn(name = "tmdb_Id")
    private MediaItem mediaItem;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Review(String title, String reviewBody, MediaItem mediaItem, User user) {
        this.title = title;
        this.reviewBody = reviewBody;
        this.mediaItem = mediaItem;
        this.user = user;
    }

    public Review() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public MediaItem getMediaItem() {
        return mediaItem;
    }

    public void setMediaItem(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
