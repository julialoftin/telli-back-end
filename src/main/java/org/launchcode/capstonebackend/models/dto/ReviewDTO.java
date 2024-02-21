package org.launchcode.capstonebackend.models.dto;

import jakarta.validation.constraints.NotNull;

public class ReviewDTO {

    @NotNull
    private String title;

    @NotNull
    private String reviewBody;

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
}
