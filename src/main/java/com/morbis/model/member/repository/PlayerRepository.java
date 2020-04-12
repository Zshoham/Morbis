package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player,Integer> {

    List<Player> findAllByNameContaining(String query);
}
