package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.WatchList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaItemRepository extends CrudRepository<MediaItem, Integer> {

//    List<MediaItem> findByMediaItem_tmdbId (int tmdbId);

    Optional<MediaItem> findByTmdbIdAndMediaType(int tmdbId, String mediaType);

}
