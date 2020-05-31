package com.morbis.model.team.repository;

import com.morbis.model.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,Integer> {

    List<Team> findAllByNameContaining(String query);

}
