package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.Player;
import com.morbis.model.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player,Integer> {

    List<Player> findAllByNameContaining(String query);

    List<Player> findAllByTeamIsNull();
}
