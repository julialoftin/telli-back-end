package org.launchcode.capstonebackend.controllers;

import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.WatchList;
import org.launchcode.capstonebackend.models.data.MediaItemRepository;
import org.launchcode.capstonebackend.models.data.WatchListRepository;
import org.launchcode.capstonebackend.models.dto.MediaItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/add-to-watchlist/{watchListId}")
    public ResponseEntity<List<MediaItem>> addMediaItemToWatchList(@PathVariable int watchListId,
                                                                   @RequestBody MediaItemDTO mediaItemDTO,
                                                                   Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);

        try {
            if (optionalWatchList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            MediaItem mediaItem = convertMediaItemDTOToEntity(mediaItemDTO);
            WatchList watchList = optionalWatchList.get();

            watchList.addMediaItemToList(mediaItem);
            watchListRepository.save(watchList);
            if (!mediaItemRepository.existsById(mediaItem.getTmdbId())) {
                mediaItemRepository.save(mediaItem);
            }
            return ResponseEntity.ok(watchList.getMediaItems());
        } catch (Exception exception) {
            System.out.println("Error adding media item to WatchList in database: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-items-in-watchlist/{watchListId}")
    public ResponseEntity<List<MediaItem>> getAllItemsInWatchList(@PathVariable int watchListId) {

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);

        try {
            if (optionalWatchList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            WatchList watchList = optionalWatchList.get();
            if (watchList.getMediaItems().isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(watchList.getMediaItems());
        } catch (Exception exception) {
            System.out.println("Error retrieving media items from WatchList: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/delete-item-from-watchlist/{watchListId}")
    public ResponseEntity<List<MediaItem>> deleteItemInWatchList(@PathVariable int watchListId,
                                                                 @RequestBody MediaItemDTO mediaItemDTO,
                                                                 Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);

        try {
            if (optionalWatchList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            WatchList watchList = optionalWatchList.get();
            MediaItem mediaItemToDelete = mediaItemRepository.findById(mediaItemDTO.getTmdbId()).orElse(null);

            if (mediaItemToDelete != null) {
                watchList.getMediaItems().remove(mediaItemToDelete);

                watchListRepository.save(watchList);
            }

            if (watchListRepository.countByMediaItemsContaining(mediaItemToDelete) == 0) {
                mediaItemRepository.delete(mediaItemToDelete);
            }

            return ResponseEntity.ok().body(watchList.getMediaItems());
        } catch (Exception exception) {
            System.out.println("Error deleting media item from WatchList: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
