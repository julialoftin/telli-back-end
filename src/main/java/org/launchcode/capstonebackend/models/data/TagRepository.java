package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);

    @Query("SELECT t FROM Tag t JOIN t.mediaItems m WHERE m.tmdbId = :tmdbId")
    List<Tag> findAllByMediaItemTmdbId(@Param("tmdbId") int tmdbId);

}
