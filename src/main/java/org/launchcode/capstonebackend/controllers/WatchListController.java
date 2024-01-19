package org.launchcode.capstonebackend.controllers;

import jakarta.validation.Valid;
import org.launchcode.capstonebackend.models.WatchList;
import org.launchcode.capstonebackend.models.data.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class WatchListController {

    @Autowired
    WatchListRepository watchListRepository;

    @PostMapping("/create-watchlist")
    public ResponseEntity<WatchList> processCreateWatchListForm(@RequestBody @Valid WatchList newWatchList, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            WatchList savedWatchList = watchListRepository.save(newWatchList);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWatchList);
        } catch (Exception exception) {
            System.out.println("Error saving WatchList to database: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-watchlists")
    public List<WatchList> getAllWatchLists() {

        List<WatchList> watchLists = (List<WatchList>) watchListRepository.findAll();

        return watchLists;
    }

}
