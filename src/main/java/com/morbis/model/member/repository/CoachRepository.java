package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoachRepository extends JpaRepository<Coach,Integer> {

    List<Coach> findAllByNameContaining(String query);

    List<Coach> findAllByTeamIsNull();
}
