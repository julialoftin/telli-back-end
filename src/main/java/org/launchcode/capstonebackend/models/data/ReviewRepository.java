package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.MediaItem;
import org.launchcode.capstonebackend.models.Review;
import org.launchcode.capstonebackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {

    List<Review> findByUser (User user);

    List<Review> findByMediaItem_tmdbId (int tmdbId);

}
