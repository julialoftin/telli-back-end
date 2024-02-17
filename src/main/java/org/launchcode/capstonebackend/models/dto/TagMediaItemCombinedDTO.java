package org.launchcode.capstonebackend.models.dto;

import jakarta.validation.constraints.NotNull;

public class TagMediaItemCombinedDTO {

    @NotNull
    private TagDTO tagDTO;

    @NotNull
    private MediaItemDTO mediaItemDTO;

    public TagDTO getTagDTO() {
        return tagDTO;
    }

    public MediaItemDTO getMediaItemDTO() {
        return mediaItemDTO;
    }

}
