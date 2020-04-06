package com.morbis.model.league.repository;

import com.morbis.model.league.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season,Integer> {
}
