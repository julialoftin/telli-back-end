package org.launchcode.capstonebackend.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.Review;
import org.launchcode.capstonebackend.models.User;
import org.launchcode.capstonebackend.models.data.MediaItemRepository;
import org.launchcode.capstonebackend.models.data.ReviewRepository;
import org.launchcode.capstonebackend.models.data.UserRepository;
import org.launchcode.capstonebackend.models.dto.MediaItemDTO;
import org.launchcode.capstonebackend.models.dto.ReviewDTO;
import org.launchcode.capstonebackend.models.dto.ReviewMediaItemCombinedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MediaItemRepository mediaItemRepository;

    @Autowired
    AuthenticationController authenticationController;

    private MediaItem convertMediaItemDTOToEntity(MediaItemDTO mediaItemDTO) {
        Optional<MediaItem> mediaItem = mediaItemRepository.findByTmdbIdAndMediaType(mediaItemDTO.getTmdbId(), mediaItemDTO.getMediaType());
        if (mediaItem.isPresent()) {
            return mediaItem.get();
        } else {
            return new MediaItem(mediaItemDTO.getTmdbId(), mediaItemDTO.getMediaType());
        }
    }

    private Review convertReviewDTOToEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setReviewBody(reviewDTO.getReviewBody());
        return review;
    }

    @PostMapping("/add")
    public ResponseEntity<Review> addReviewToMediaItem(@RequestBody @Valid ReviewMediaItemCombinedDTO reviewMediaItemCombinedDTO,
                                                       Errors errors, HttpSession session) {
        try {
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().build();
            }

            User user = authenticationController.getUserFromSession(session);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Review review = convertReviewDTOToEntity(reviewMediaItemCombinedDTO.getReviewDTO());
            MediaItem mediaItem = convertMediaItemDTOToEntity(reviewMediaItemCombinedDTO.getMediaItemDTO());

            if (!mediaItemRepository.existsById(mediaItem.getTmdbId())) {
                mediaItemRepository.save(mediaItem);
            }

            review.setUser(user);
            review.setMediaItem(mediaItem);
            reviewRepository.save(review);

            return ResponseEntity.ok().body(review);
        } catch (Exception exception) {
            System.out.println("Error getting reviews: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get-reviews-by-media-item/{tmdbId}")
    public ResponseEntity<List<Review>> getAllReviewsByMediaItem(@PathVariable int tmdbId) {
        try {

            if (reviewRepository.findByMediaItem_tmdbId(tmdbId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok().body(reviewRepository.findByMediaItem_tmdbId(tmdbId));
        } catch (Exception exception) {
            System.out.println("Error getting reviews: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint to list all Reviews a logged-in User has made
    @GetMapping("get-reviews-by-user")
    public ResponseEntity<List<Review>> getAllReviewsByUser(HttpSession session) {
        User user = authenticationController.getUserFromSession(session);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (reviewRepository.findByUser(user).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok().body(reviewRepository.findByUser(user));

    }

    // Endpoint for a user to delete a review they have made
    @DeleteMapping("delete/{reviewId}")
    public ResponseEntity<Review> deleteReview(@PathVariable int reviewId, HttpSession session) {
        try {
            User user = authenticationController.getUserFromSession(session);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Optional<Review> optionalReview = reviewRepository.findById(reviewId);
            if (optionalReview.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Review review = optionalReview.get();
            if (!review.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            reviewRepository.deleteById(review.getId());

            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            System.out.println("Error deleting review: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
