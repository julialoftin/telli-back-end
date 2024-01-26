package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.WatchList;
import org.springframework.data.repository.CrudRepository;

public interface MediaItemRepository extends CrudRepository<MediaItem, Integer> {

}
