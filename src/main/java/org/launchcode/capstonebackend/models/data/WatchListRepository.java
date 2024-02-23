package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.User;
import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.WatchList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepository extends CrudRepository<WatchList, Integer> {

    List<WatchList> findByUser(User user);

    int countByMediaItemsContaining(MediaItem mediaItem);

    @Query("SELECT w.mediaItems FROM WatchList w WHERE w.id = :watchListId")
    List<MediaItem> findAllMediaItemsByWatchListId(@Param("watchListId") int watchListId);

}
