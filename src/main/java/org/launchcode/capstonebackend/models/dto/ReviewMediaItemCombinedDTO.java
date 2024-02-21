package org.launchcode.capstonebackend.models.dto;

import jakarta.validation.constraints.NotNull;

public class ReviewMediaItemCombinedDTO {

    @NotNull
    private ReviewDTO reviewDTO;

    @NotNull
    private MediaItemDTO mediaItemDTO;

    public ReviewDTO getReviewDTO() {
        return reviewDTO;
    }

    public MediaItemDTO getMediaItemDTO() {
        return mediaItemDTO;
    }

}
