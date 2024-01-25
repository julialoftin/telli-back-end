package org.launchcode.capstonebackend.controllers;

import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.WatchList;
import org.launchcode.capstonebackend.models.data.MediaItemRepository;
import org.launchcode.capstonebackend.models.data.WatchListRepository;
import org.launchcode.capstonebackend.models.dto.MediaItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/media-item")
public class MediaItemController {

    @Autowired
    MediaItemRepository mediaItemRepository;

    @Autowired
    WatchListRepository watchListRepository;

    private MediaItem convertMediaItemDTOToEntity(MediaItemDTO mediaItemDTO) {
        MediaItem mediaItem = new MediaItem(mediaItemDTO.getTmdbId(), mediaItemDTO.getMediaType());
        return mediaItem;
    }

    @PostMapping("/add-media-item-to-watchlist/{watchListId}")
    public ResponseEntity<List<MediaItem>> addMediaItemToWatchList(@PathVariable int watchListId,
                                                                   @RequestBody MediaItemDTO mediaItemDTO,
                                                                   Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        if (optionalWatchList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            MediaItem mediaItem = convertMediaItemDTOToEntity(mediaItemDTO);
            mediaItemRepository.save(mediaItem);

            WatchList watchList = optionalWatchList.get();
            watchList.addMediaItemToList(mediaItem);
            watchListRepository.save(watchList);

            return ResponseEntity.ok(watchList.getMediaItems());
        }

    }

}
