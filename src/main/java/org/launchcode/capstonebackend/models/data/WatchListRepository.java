package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.User;
import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.WatchList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepository extends CrudRepository<WatchList, Integer> {

    List<WatchList> findByUser(User user);

    int countByMediaItemsContaining(MediaItem mediaItem);

}
