package org.launchcode.capstonebackend.controllers;

import org.launchcode.capstonebackend.models.WatchList;
import org.launchcode.capstonebackend.models.data.MediaItemRepository;
import org.launchcode.capstonebackend.models.data.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/media-item")
public class MediaItemController {

    @Autowired
    MediaItemRepository mediaItemRepository;

    @Autowired
    WatchListRepository watchListRepository;

    @PostMapping("/add-media-item-to-watchlist/{watchListId}/{tmdbId}/{mediaType}")
    public ResponseEntity<WatchList> addMediaItemToWatchList(@PathVariable int watchListId,
                                                             @PathVariable int tmdbId,
                                                             @PathVariable String mediaType) {

    }

}
