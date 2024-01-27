package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.MediaItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaItemRepository extends CrudRepository<MediaItem, Integer> {
}
