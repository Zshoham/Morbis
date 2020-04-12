package com.morbis.model.member.repository;

import com.morbis.model.member.entity.TeamManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamManagerRepository extends JpaRepository<TeamManager,Integer> {

    List<TeamManager> findAllByNameContaining(String query);
}
