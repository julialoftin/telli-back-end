package org.launchcode.capstonebackend.controllers;

import jakarta.validation.Valid;
import org.launchcode.capstonebackend.models.WatchList;
import org.launchcode.capstonebackend.models.data.WatchListRepository;
import org.launchcode.capstonebackend.models.dto.EditWatchListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<WatchList>> getAllWatchLists() {
        List<WatchList> watchLists = (List<WatchList>) watchListRepository.findAll();

        if (watchLists.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(watchLists);
        }
    }

    @PutMapping("/edit-watchlist/{watchListId}")
    public ResponseEntity<WatchList> editWatchListDetails(@PathVariable int watchListId,
                                                          @RequestBody @Valid EditWatchListDTO editWatchListDTO,
                                                          Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);

        try {

            if (optionalWatchList.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                WatchList watchListToEdit = optionalWatchList.get();
                watchListToEdit.setName(editWatchListDTO.getNewName());
                watchListToEdit.setDescription(editWatchListDTO.getNewDescription());
                watchListRepository.save(watchListToEdit);

                return ResponseEntity.ok(watchListToEdit);
            }

        } catch (Exception exception) {
            System.out.println("Error updating WatchList in database: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
