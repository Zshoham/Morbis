package com.morbis.model.team.repository;

import com.morbis.model.team.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StadiumRepository extends JpaRepository<Stadium,Integer> {

    List<Stadium> findAllByNameContaining(String query);
}
