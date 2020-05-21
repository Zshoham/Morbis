package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Coach;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach,Integer> {

    List<Coach> findAllByNameContaining(String query);

    List<Coach> findAllByTeamIsNull();
}
