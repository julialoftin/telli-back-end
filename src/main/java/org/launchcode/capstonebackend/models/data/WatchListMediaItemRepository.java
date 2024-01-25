package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.WatchListMediaItem;
import org.springframework.data.repository.CrudRepository;

public interface WatchListMediaItemRepository extends CrudRepository<WatchListMediaItem, Integer> {
}
