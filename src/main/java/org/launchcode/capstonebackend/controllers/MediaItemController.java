package org.launchcode.capstonebackend.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.User;
import org.launchcode.capstonebackend.models.WatchList;
import org.launchcode.capstonebackend.models.data.MediaItemRepository;
import org.launchcode.capstonebackend.models.data.WatchListRepository;
import org.launchcode.capstonebackend.models.dto.MediaItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/media-item")
public class MediaItemController {

    @Autowired
    MediaItemRepository mediaItemRepository;

    @Autowired
    WatchListRepository watchListRepository;

    @Autowired
    AuthenticationController authenticationController;

    private MediaItem convertMediaItemDTOToEntity(MediaItemDTO mediaItemDTO) {
        Optional<MediaItem> existingMediaItem = mediaItemRepository.findById(mediaItemDTO.getTmdbId());
        return existingMediaItem.orElseGet(() -> new MediaItem(mediaItemDTO.getTmdbId(), mediaItemDTO.getMediaType()));
    }

    @PostMapping("/add-to-watchlist/{watchListId}")
    public ResponseEntity<List<MediaItem>> addMediaItemToWatchList(@PathVariable int watchListId,
                                                                   @RequestBody @Valid MediaItemDTO mediaItemDTO,
                                                                   HttpSession session, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);

        try {
            if (optionalWatchList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            WatchList watchList = optionalWatchList.get();
            if (!watchList.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            MediaItem mediaItem = convertMediaItemDTOToEntity(mediaItemDTO);
            // Checks if watchlist already contains media item
            List<MediaItem> mediaItems = watchListRepository.findAllMediaItemsByWatchListId(watchList.getId());
            if (mediaItems.contains(mediaItem)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
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
    public ResponseEntity<List<MediaItem>> getAllItemsInWatchList(@PathVariable int watchListId, HttpSession session) {

        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

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
                                                                 @RequestBody @Valid MediaItemDTO mediaItemDTO,
                                                                 HttpSession session, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);

        try {
            if (optionalWatchList.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            WatchList watchList = optionalWatchList.get();
            if (!watchList.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            MediaItem mediaItemToDelete = mediaItemRepository.findById(mediaItemDTO.getTmdbId()).orElse(null);

            if (mediaItemToDelete != null) {
                watchList.getMediaItems().remove(mediaItemToDelete);

                watchListRepository.save(watchList);

                if (watchListRepository.countByMediaItemsContaining(mediaItemToDelete) == 0) {
                    mediaItemRepository.delete(mediaItemToDelete);
                }
            }

            return ResponseEntity.ok().body(watchList.getMediaItems());
        } catch (Exception exception) {
            System.out.println("Error deleting media item from WatchList: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
