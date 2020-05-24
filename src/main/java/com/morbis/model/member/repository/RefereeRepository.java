package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefereeRepository extends JpaRepository<Referee,Integer> {

    List<Referee> findAllByNameContaining(String query);
}
