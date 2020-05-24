package com.morbis.model.team.repository;

import com.morbis.model.team.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StadiumRepository extends JpaRepository<Stadium,Integer> {

    Optional<Stadium> findDistinctByName(String name);

    List<Stadium> findAllByNameContaining(String query);

    List<Stadium> findAllByTeamIsNull();
}
