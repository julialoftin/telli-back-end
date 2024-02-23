package org.launchcode.capstonebackend.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.Tag;
import org.launchcode.capstonebackend.models.User;
import org.launchcode.capstonebackend.models.data.MediaItemRepository;
import org.launchcode.capstonebackend.models.data.TagRepository;
import org.launchcode.capstonebackend.models.dto.MediaItemDTO;
import org.launchcode.capstonebackend.models.dto.TagDTO;
import org.launchcode.capstonebackend.models.dto.TagMediaItemCombinedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    MediaItemRepository mediaItemRepository;

    @Autowired
    TagRepository tagRepository;

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

    private Tag convertTagDTOToEntity(TagDTO tagDTO) {
        Optional<Tag> existingTag = tagRepository.findByName(tagDTO.getName());
        if (existingTag.isPresent()) {
            return existingTag.get();
        }
        Tag tag = new Tag(tagDTO.getName());
        tagRepository.save(tag);

        return tag;
    }

    // Creates tag, but does not associate it with a media item
    @PostMapping("/create")
    public ResponseEntity<Tag> createTag(@RequestBody @Valid TagDTO tagDTO,
                                         Errors errors, HttpSession session) {
        try {
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().build();
            }
            User user = authenticationController.getUserFromSession(session);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Checks if a tag of the same name exists
            Optional<Tag> existingTag = tagRepository.findByName(tagDTO.getName());
            if (existingTag.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Creates a new tag
            Tag tag = convertTagDTOToEntity(tagDTO);

            return ResponseEntity.ok().body(tag);
        } catch (Exception exception) {
            System.out.println("Error saving tag: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add-to-media-item")
    public ResponseEntity<List<Tag>> addTagToMediaItem(@RequestBody @Valid TagMediaItemCombinedDTO tagMediaItemCombinedDTO,
                                                       Errors errors, HttpSession session) {
        try {
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().build();
            }
            User user = authenticationController.getUserFromSession(session);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Tag tag = convertTagDTOToEntity(tagMediaItemCombinedDTO.getTagDTO());
            MediaItem mediaItem = convertMediaItemDTOToEntity(tagMediaItemCombinedDTO.getMediaItemDTO());

            mediaItem.addTag(tag);
            mediaItemRepository.save(mediaItem);

            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            System.out.println("Error saving tag: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Tag>> getAllTags() {
        if (tagRepository.findAllTags().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(tagRepository.findAllTags());
    }

    @GetMapping("/get-tags-by-media-item")
    public ResponseEntity<List<Tag>> getTagsByMediaItem(@RequestBody MediaItemDTO mediaItemDTO, Errors errors) {
        try {
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().build();
            }
            if (tagRepository.findByMediaItems_tmdbIdAndMediaItems_mediaType(mediaItemDTO.getTmdbId(), mediaItemDTO.getMediaType()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok().body(tagRepository.findByMediaItems_tmdbIdAndMediaItems_mediaType(mediaItemDTO.getTmdbId(), mediaItemDTO.getMediaType()));
        } catch (Exception exception) {
            System.out.println("Error saving retrieving tags: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get-all-media-items-by-tag/{tagId}")
    public ResponseEntity<List<MediaItem>> getAllMediaItemsByTag(@PathVariable int tagId) {
        try {
            if (tagRepository.findAllMediaItemsByTagId(tagId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok().body(tagRepository.findAllMediaItemsByTagId(tagId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
