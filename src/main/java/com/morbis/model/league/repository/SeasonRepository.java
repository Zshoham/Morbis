package com.morbis.model.league.repository;

import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season,Integer> {

    List<Season> findAllByYear(int query);

    Optional<Season> findByLeagueAndYear(League league, int year);
}
