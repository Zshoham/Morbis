package com.morbis.model.game.repository;

import com.morbis.model.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game,Integer> {

    @Query("select g from Game g join fetch g.home join fetch g.away where " +
            "g.home.name like %:query% or g.away.name like %:query%")
    List<Game> findAllContainingQuery(String query);
}
