package org.launchcode.capstonebackend.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.capstonebackend.models.User;
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
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    WatchListRepository watchListRepository;

    @Autowired
    AuthenticationController authenticationController;

    @PostMapping("/create")
    public ResponseEntity<WatchList> processCreateWatchListForm(@RequestBody @Valid WatchList newWatchList,
                                                                HttpSession session, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            newWatchList.setUser(user);
            WatchList savedWatchList = watchListRepository.save(newWatchList);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedWatchList);
        } catch (Exception exception) {
            System.out.println("Error saving WatchList to database: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<WatchList>> getAllWatchLists(HttpSession session) {
        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<WatchList> watchLists = watchListRepository.findByUser(user);

        if (watchLists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(watchLists);
    }

    @GetMapping("get-by-id/{watchListId}")
    public ResponseEntity<WatchList> getWatchListById(@PathVariable int watchListId, HttpSession session) {
        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);
        if (optionalWatchList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(optionalWatchList.get());
    }

    @PutMapping("/edit-watchlist/{watchListId}")
    public ResponseEntity<WatchList> editWatchListDetails(@PathVariable int watchListId,
                                                          @RequestBody @Valid EditWatchListDTO editWatchListDTO,
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
            } else {
                WatchList watchListToEdit = optionalWatchList.get();
                if (!watchListToEdit.getUser().equals(user)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
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

    @DeleteMapping("/delete-watchlist/{watchListId}")
    public ResponseEntity<WatchList> deleteWatchList(@PathVariable int watchListId, HttpSession session) {

        User user = authenticationController.getUserFromSession(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<WatchList> optionalWatchList = watchListRepository.findById(watchListId);

        try {
            if (optionalWatchList.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                WatchList watchList = optionalWatchList.get();
                if (!watchList.getUser().equals(user)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                watchListRepository.delete(watchList);
                return ResponseEntity.ok().build();
            }
        } catch (Exception exception) {
            System.out.println("Error deleting WatchList from database: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
