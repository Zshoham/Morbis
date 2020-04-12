package com.morbis.model.league.repository;

import com.morbis.model.league.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League,Integer> {

    List<League> findAllByNameContaining(String query);
}
